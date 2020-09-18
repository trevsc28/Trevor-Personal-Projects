#include<stdlib.h>
#include<stdio.h>
#include<string.h>

typedef struct Node {
	int value, order;
	char* name;
	struct Node* next;
} Node;


int NOT(int);
int AND(int, int);
int OR(int, int);
int NOR(int, int);
int NAND(int, int);
int XOR(int, int);
int XNOR(int, int);

int* getGrayArray(int, int);
int isNumeric(char*);
Node* addVariableToList(Node*, int, int, char*);
int getVariableFromList(Node*, char*);
void generateEmptyGrayCode();
Node* updateByName(Node*, char*, int);
Node* updateByOrder(Node*, int, int);
char* getVariableByOrder(Node*, int);
void freeNodes(Node*);

Node* inputs = NULL;
int in, out;
int** graycode;
char* bank[5000];
int count = 0;

	int main(int argc, char** argv){
		FILE* file = fopen(argv[1], "r");
		if (file == NULL) { printf("invalid file\n"); return 0; }	
	
		//REGISTER INPUTS
		fscanf(file, "INPUTVAR %d ", &in);
		for (int i = 0; i < in; i++){
		char* inv = malloc(sizeof(char*));
			fscanf(file, " %s ", inv);
			inputs = addVariableToList(inputs, i+1, -2, inv);
		}
		
		//REGISTER OUTPUTS	
		fscanf(file, "OUTPUTVAR %d ", &out);
		for (int i = 0; i < out; i++){
		char* onv = malloc(sizeof(char*));
			fscanf(file, " %s ", onv);
			inputs = addVariableToList(inputs, -1 * (i+1), -2, onv);
		}
		//GENERATE BASE GRAYCODE
		graycode = malloc((1<<in) * sizeof(int*));
		for (int i = 0; i < (1<<in); i++)
			graycode[i] = malloc((in+out) * sizeof(int));
		generateEmptyGrayCode();		
		char* move = malloc(22); 

			while (fscanf(file, "%s", move) != EOF){
				bank[count] = move;
				count++;
				move = malloc(22); //max str
			}

		int one, two;
		for (int i = 0; i < (1<<in); i++){ //edit gray code row by row	
			for (int j = 0; j < in; j++)
				inputs = updateByOrder(inputs, j+1, graycode[i][j]);

			int index = 0;
			while (index < count){
				if (strcmp(bank[index], "AND") == 0){
					one = getVariableFromList(inputs, bank[index+1]);
					two = getVariableFromList(inputs, bank[index+2]);
					if (one == -1) one = atoi(bank[index+1]);
					if (two == -1) two = atoi(bank[index+2]);
					inputs = updateByName(inputs, bank[index+3], AND(one, two));
					index = index + 3;
				  } else if (strcmp(bank[index], "OR") == 0){	
					one = getVariableFromList(inputs, bank[index+1]);
					two = getVariableFromList(inputs, bank[index+2]);
					if (one == -1) one = atoi(bank[index+1]);
					if (two == -1) two = atoi(bank[index+2]);
					inputs = updateByName(inputs, bank[index+3], OR(one, two));
					index = index+3;
				  } else if (strcmp(bank[index], "NOT") == 0){ 
				  	one = getVariableFromList(inputs, bank[index+1]);
					if (one == -1) one = atoi(bank[index+1]);
					inputs = updateByName(inputs, bank[index+2], NOT(one));
					index = index + 2;
				  } else if (strcmp(bank[index], "NAND") == 0){	
				  	one = getVariableFromList(inputs, bank[index+1]);
					two = getVariableFromList(inputs, bank[index+2]);
					if (one == -1) one = atoi(bank[index+1]);
					if (two == -1) two = atoi(bank[index+2]);
					inputs = updateByName(inputs, bank[index+3], NAND(one, two));
					index = index + 3;
				  } else if (strcmp(bank[index], "NOR") == 0){	
				  	one = getVariableFromList(inputs, bank[index+1]);
					two = getVariableFromList(inputs, bank[index+2]);
					if (one == -1) one = atoi(bank[index+1]);
					if (two == -1) two = atoi(bank[index+2]);
					inputs = updateByName(inputs, bank[index+3], NOR(one, two));
				  	index = index + 3;
				  } else if (strcmp(bank[index], "XOR") == 0){
				  	one = getVariableFromList(inputs, bank[index+1]);
					two = getVariableFromList(inputs, bank[index+2]);
					if (one == -1) one = atoi(bank[index+1]);
					if (two == -1) two = atoi(bank[index+2]);
					inputs = updateByName(inputs, bank[index+3], XOR(one, two));
					index = index + 3;
                                  } else if (strcmp(bank[index], "XNOR") == 0){
				  	one = getVariableFromList(inputs, bank[index+1]);
					two = getVariableFromList(inputs, bank[index+2]);
					if (one == -1) one = atoi(bank[index+1]);
					if (two == -1) two = atoi(bank[index+2]);
					inputs = updateByName(inputs, bank[index+3], XNOR(one, two));
					index = index + 3;
                                  } else if (strcmp(bank[index], "DECODER") == 0){
				  	int bits = atoi(bank[index+1]);
					int tag = 0, hit = 0;
					for (int r = (index+2+bits); r < (index+2+bits)+(1 << bits); r++){ //lets us look at each output value
						int* binArr = getGrayArray(bits, tag);	
						tag++;
						hit = 0;
						for (int s = 0; s < bits; s++){
						int tmp = 0;
						if (isNumeric(bank[index+2+s]) == 1) tmp = atoi(bank[index+2+s]);
						else tmp = getVariableFromList(inputs, bank[index+2+s]);
							if (binArr[s] != tmp){
								inputs = updateByName(inputs, bank[r], 0);
								hit=1;
							}
						}
						if (hit == 0) //no contradiction, must be true
							inputs = updateByName(inputs, bank[r], 1);
						free(binArr);
					}
				  } else if (strcmp(bank[index], "MULTIPLEXER") == 0){
					int num_inputs = atoi(bank[index+1]);
					int num_selectors = 0;
					int temp = num_inputs;
					while (temp > 1){
						temp = temp/2;		
						num_selectors++;
					} 
					int total = 0;
					for (int i = 0; i <  (num_selectors); i++){
						int val = getVariableFromList(inputs, bank[index+num_inputs+2+i]);
						if (val == -1) val = atoi(bank[index+num_inputs+2+i]);
						total = total + (val * (1 << (num_selectors-1-i)));
					}
					int v = 0;
					for (int t = total; t > 0; t = t >> 1)
						v = v ^ t;
					if (isNumeric(bank[index+2+v]) == 1)
						inputs = updateByName(inputs, bank[index+2+num_inputs+num_selectors], atoi(bank[index+2+v]));
					else {
						int q = getVariableFromList(inputs, bank[index+2+v]);
						inputs = updateByName(inputs, bank[index+2+num_inputs+num_selectors], q);
					}
					index = index + 2+num_inputs+num_selectors;

				  }  
				index++;
			}	
			for (int k = 0; k < out; k++)
				graycode[i][k+in] = getVariableFromList(inputs, getVariableByOrder(inputs, -(k+1)));	
		}	
		for (int n = 0; n < (1<<in); n++){
			for (int m = 0; m < (in+out); m++)
				printf("%d ", graycode[n][m]);
			printf("\n");
		}	
		freeNodes(inputs);
		for (int i = 0; i < (1<<in); i++)
			free(graycode[i]);
		free(graycode);
		for (int i = 0; i < count; i++)
			free(bank[i]);
	
		return 0;
	}

	int NOT(int n){
		if (n == 1) 
			return 0;
		return 1;
	}	
	int AND(int x, int y){
		return x&y;
	}
	int OR(int x, int y){
		return x|y;
	}
	int NAND(int x, int y){
		if (AND(x, y) == 1) 
			return 0;
		else return 1;
	}
	int NOR(int x, int y){
		if (OR(x, y) == 1)
			return 0;
		else return 1;
	}
	int XOR(int x, int y){
		return x ^ y;
	}
	int XNOR(int x, int y){
		return x == y;
	}
	
	
	Node* addVariableToList(Node* head, int order, int value, char* name){
		Node* insert  = malloc(sizeof(Node));
		insert->name = name;
		insert->value = value;
		insert->next = head;
		insert->order = order;
		head = insert;		
		return head;
	}
	int getVariableFromList(Node* head, char* name){
		Node* curr = head;
		while (curr != NULL && name != NULL){
			if (curr->name != NULL && strcmp(name, curr->name) == 0)
				return curr->value;
			curr = curr->next;
		}
		return -1;
	}
	char* getVariableByOrder(Node* head, int order){
		Node* curr = head;
		while (curr != NULL){
			if (curr->order == order)
				return curr->name;
			curr = curr->next;
		}
	return NULL;
	}
	Node* updateByOrder(Node* head, int order, int value){
		Node* curr = head;
		while (curr != NULL){
			if (curr->order == order){
				curr->value = value;
				return head;
			}
			curr = curr->next;
		}
		return head;
	}
	Node* updateByName(Node* head, char* name, int value){
		Node* curr = head;
		while (curr != NULL){
			if (name != NULL && strcmp(curr->name, name) == 0){
				curr->value = value;
				return head;
			}
			curr = curr->next;
		}
		return addVariableToList(inputs, 0, value, name);
	}
	void generateEmptyGrayCode(){	
		for (int j = 0; j < (1<<in); j++){
			graycode[j][(in+out)-1] = 0;
		}
		for (int i = 0; i < (1<<in); i++){
			int num = i ^ (i >> 1); //dec - > dec gray
			int index = in-1;
			while (num > 0){//gray -> binary gray
				graycode[i][index] = num % 2;
				num = num / 2;
				index--;
			}
		}	
	}
	int* getGrayArray(int bits, int n){
		int* ret = malloc(bits * sizeof(int));
		for (int i = 0; i < bits; i++)
			ret[i] = 0;
		int index = bits-1;
		n = (n ^ (n >> 1));
		while (n > 0){
			ret[index] = n%2;
			n = n / 2;
			index--;
		}
		return ret; 
	}
	int isNumeric(char* n){
		if (strspn(n, "01") == strlen(n))
			return 1;
		return 0;
	}
	void freeNodes(Node* n){
		Node* temp = n;
		while (n != NULL){
			temp = n;
			n = n->next;
			free(temp);
		}
	}
	
