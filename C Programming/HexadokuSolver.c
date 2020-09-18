#include<stdlib.h>
#include<stdio.h>

void printArray(char**);
int ctoh(char);
char htoc(int);

int main(int argc, char** argv){
	FILE* file = fopen(argv[1], "r");
	if (file == NULL) { printf("error\n"); exit(0); }
	char** matrix = malloc(16 * sizeof(int*));
	for (int i = 0; i < 16; i++)
		matrix[i] = malloc(16 * sizeof(int));	
	for (int i = 0; i < 16; i++){
		for (int j = 0; j < 16; j++)
			fscanf(file, "%c\t", &matrix[i][j]);
	}	
	int go = 1;
	while (go == 1){
		int found, line, index;
		go = -1;
		for (int i = 0; i < 16; i++){//row
			found = 0;
			line = 120;
			index = -1;
			for (int j = 0; j < 16; j++){
				if (matrix[i][j] != '-')
					line = line - ctoh(matrix[i][j]);
				else if (index != -1) 
					found++;
				else{index = j; found++;}
			}
			if (found == 1){
				matrix[i][index] = htoc(line);	
				go = 1;
			}		
		}
		for (int i = 0; i < 16; i++){ //columns
			found = 0;
			line = 120;
			index = -1;
			for (int j = 0; j < 16; j++){
				if (matrix[j][i] != '-')
					line = line - ctoh(matrix[j][i]);
				else if (index != -1) 
					found++;
				else { index = j; found++;}
			}
			if (found == 1){
				matrix[index][i] = htoc(line);
				go = 1;
			}
			
		}
		
		int sI = 0, sJ = 0;
		while (sJ < 16){
			int pos = 0;
			int posI, posJ;
			line = 120;
			for (int i = sI; i  < 4+sI; i++){ 
				for (int j = sJ; j < 4+sJ; j++){
					if (matrix[i][j] != '-')
						line = line - ctoh(matrix[i][j]);
					else if (pos == 0){
						pos = 1;
						posI = i;		
						posJ = j;
					}
					else
						pos = -1;		
				}
			}
			if (pos > 0){
				matrix[posI][posJ] = htoc(line);
				go = 1;
			}

			sI = sI + 4;
			if (sI == 16){ sI = 0; sJ = sJ + 4; }
		}
	}
		//interprative check
		int *rw = malloc(16 * sizeof(int));
		
		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				if (matrix[i][j] == '-'){
					for (int k = 0; k < 16; k++){ //row secret
						if(matrix[k][j] != '-')
							rw[ctoh(matrix[k][j])]++; 
					}
					for (int l = 0; l < 16; l++){//column secret
						if (matrix[i][l] != '-')
							rw[ctoh(matrix[i][l])]++;
					}
					for (int m = ((i/4)*4); m < ((i/4)*4)+4; m++)
						for (int n = ((j/4)*4); n < ((j/4)*4)+4; n++)
							if (matrix[m][n] != '-')
								rw[ctoh(matrix[m][n])]++;

					
					for (int o = 0; o < 16; o++){
						
						if (rw[o] == 0){
							matrix[i][j] = htoc(o);
							
						}
					}
					for (int p = 0; p < 16; p++)
						rw[p] = 0;
				}				
			}
		}
		//simple check
		for (int i = 0; i < 16; i++)
		for (int j = 0; j < 16; j++)
			if (matrix[i][j] == '-'){
				printf("no-solution\n");
				for (int k = 0; k < 16; k++)
					if (matrix[k] != NULL)
						free(matrix[k]);
				exit(0);	
			}
	int good = 1;
	//advanced check
	int *rwx = malloc(16*sizeof(int));
	int *cl = malloc(16*sizeof(int));
	
	for (int i = 0; i < 16; i++){
		for (int j = 0; j < 16; j++){
			rwx[ctoh(matrix[i][j])]++;
			cl[ctoh(matrix[j][i])]++; 
		}
		for (int x = 0; x < 16; x++){
			if (rwx[x] != 1)
				good = -1;
			if (cl[x] != 1)
				good = -1;
			rwx[x] = 0;
			cl[x] = 0;
		}
	}
	//check for boxes
	int xI = 0;
	int xJ = 0;
	int line = 120;
	while (xJ < 16){
		line = 120;
		for (int i = xI; i < 4+xI; i++){
			for (int j = xJ; j < 4+xJ; j++){
				line = line - ctoh(matrix[i][j]);
			}
		}
		if (line != 0)
			good = -1;

		xI = xI + 4;
		if (xI == 16){xI = 0; xJ = xJ + 4; }
	}
		
	if (good != 1){//bad! 
		printf("no-solution");
	}else {
		printArray(matrix);
	}

	free(rw);
	free(rwx);
	free(cl);
	fclose(file);
	for (int i = 0; i < 16; i++){
		if (matrix[i] != NULL)
		free(matrix[i]);
	}

	return 0;
}

void printArray(char** matrix){
	for (int i = 0; i < 16; i++){
		for (int j = 0; j < 16; j++){
		if (matrix[i][j] != '-')
			printf("%c\t", matrix[i][j]);
		else printf("-");
		}
		printf("\n");
	}
}
int ctoh(char c){
	switch (c){
		case 'A':
			return 0xA;
		case 'B':
			return 0xB;
		case 'C':
			return 0xC;
		case 'D':
			return 0xD;
		case 'E':
			return 0xE;
		case 'F':
			return 0xF;	
		case '-':
			return 16;
		default:
			return c - '0';
	}
}
char htoc(int i){
	switch (i){
		case 10:
			return 'A';
		case 11:
			return 'B';
		case 12:
			return 'C';
		case 13:
			return 'D';
		case 14:
			return 'E';
		case 15:
			return 'F';
		default:
			return i + '0';
	}
}
