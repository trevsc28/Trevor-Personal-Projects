#include<stdlib.h>
#include<stdio.h>
double** inverse(double**, int);
double** transpose(double**, int, int);
double** multiply(double**, double**, int, int, int, int);
double** divideBy(double**, int, int, double);
double** subtractBy(double**, int, int, int);
int main(int argc, char** argv){
	FILE *train = fopen(argv[1], "r");
	FILE *test = fopen(argv[2], "r");
	if (train == NULL || test == NULL){printf("error\n"); exit(0);}

	int attributes, examples, entries;
	fscanf(train, "%d\n", &attributes);
	fscanf(train, "%d\n", &examples);
	fscanf(test, "%d\n", &entries);

	double **x = malloc(examples * sizeof(double*));
	double **y = malloc(examples * sizeof(double*));

	for (int i=0; i<examples; i++){
		x[i] = malloc((attributes+1) * sizeof(double));
		y[i] = malloc(sizeof(double));
	}
	for (int i=0; i<examples; i++){
		for (int j=0; j<=attributes+1; j++){
			if (j == 0) x[i][j] = 1;
			else if (attributes+1 == j)
			     fscanf(train, "%lf,", &y[i][0]);
			else
			     fscanf(train, "%lf,", &x[i][j]);	
		}
	}
	int x_rows = examples, x_cols = attributes+1;
	int y_rows = examples, y_cols = 1;
	double **w = inverse(multiply(transpose(x, x_rows, x_cols), x, x_cols, x_rows, x_rows, x_cols), x_cols);
	int w_rows = x_cols, w_cols = x_cols;
	w = multiply(w, transpose(x, x_rows, x_cols), w_rows, w_cols, x_cols, x_rows),
	w_cols = x_rows;
	w = multiply(w, y, w_rows, w_cols, y_rows, y_cols); 
	w_cols = 1;
	
	double** entr = malloc(entries * sizeof(double*));
	for (int i = 0; i < entries; i++)
	  entr[i] = malloc((attributes+1) * sizeof(double));
	
	for (int i = 0; i < entries; i++)
	  for (int j = 0; j < attributes+1; j++){
	    if (j == 0)
	      entr[i][j] = 1;
	    else
	      fscanf(test, "%lf,", &entr[i][j]);
	  }	
	double** answer = multiply(entr, w, entries, attributes+1, w_rows, w_cols);
	for (int i = 0 ; i  < entries; i++)
	  for (int k = 0; k < w_cols; k++)
	    printf("%0.0lf\n", answer[i][k]);
	fclose(train);
	fclose(test);
	for (int i = 0; i < entries; i++){
	  free(entr[i]);
	  free(answer[i]);
	  free(x[i]);
	  free(y[i]);
	}
	free(entr); 
	free(answer);
	free(x);
	free(y);
	free(w);
	return 0;
}
double** transpose(double **matrix, int rows, int cols){
	double **arr = malloc(cols* sizeof(double*));
	for (int i = 0; i < cols; i++)
		arr[i] = malloc(rows* sizeof(double));
	for (int i = 0; i < rows; i++)
		for (int j = 0; j < cols; j++)
			arr[j][i] = matrix[i][j];
	return arr;
}
double** multiply(double** xt, double** x, int row1, int col1, int row2, int col2){
	double **mp = malloc(row1 * sizeof(double*));
	for (int i = 0; i < row1; i++)
		mp[i] = malloc(col2 * sizeof(double));
	for (int i = 0; i < row1; i++)
	for (int j = 0; j < col2; j++)
	for (int k = 0; k < row2; k++)
		mp[i][j]+=(xt[i][k]*x[k][j]); 
	return mp;
}
double** inverse(double** inv, int size){
	double **in = malloc((size) * sizeof(double*));
	for (int i = 0; i <size; i++)
		in[i] = malloc(size*2 * sizeof(double));
	for (int i = 0; i < size; i++)
		for (int j = 0; j < size*2; j++){
			if (i+size == j) in[i][j] = 1;
			else if (j<size) in[i][j] = inv[i][j];
			else in[i][j] = 0;
		}
	for (int i = 0; i < size; i++){
		for (int j = 0; j < size; j++)
			in = divideBy(in, size, j, in[j][i]);
		for (int j = 0; j < size; j++){
			if (j != i && in[i][j] != 0)
				in = subtractBy(in, size,j,i);  
		}
	}	
	for (int i = size-1; i >= 0; i--){
		for (int j = i; j >= 0; j--)
			in = divideBy(in, size, j, in[j][i]);
		for (int k = i-1; k >= 0; k--){
			if (in[k][i] != 0)
				in = subtractBy(in, size, k, i);
		}
	}
	for (int i = 0; i < size; i++)
		for (int j = 0; j < size; j++)
			inv[i][j] = in[i][j+size];	
	free(in);	
	return inv;
}
double** divideBy(double** matrix, int size, int row, double divisor){
	if (divisor == 0) return matrix;
	for (int i = 0; i < size * 2; i++)
		matrix[row][i] = matrix[row][i]/divisor;
	return matrix;
}
double** subtractBy(double** matrix, int size, int row1, int row2){ 
	for (int i = 0; i < size * 2; i++)
		matrix[row1][i] = matrix[row1][i] - matrix[row2][i];
	return matrix;
}
