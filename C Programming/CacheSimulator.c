#include<stdlib.h>
#include<stdio.h>
#include<string.h>
#include<math.h>
typedef struct{
	unsigned long int tag;
	unsigned int valid;
	unsigned int timestamp;
} Node;

int isValidSize(int);
void throwError();
Node** parse(Node**, int, char);
Node** prefetch(Node**, int, char);
Node** dirprefetch(Node** cached, int address, char op);
int cache_size, block_size, associativity, prefetch_size, count = 1;
int sets, block_bits, index_bits;
int NP_MemoryReads = 0, NP_MemoryWrites=0, NP_CacheHits=0, NP_CacheMisses=0;
int P_MemoryReads = 0, P_MemoryWrites=0, P_CacheHits=0, P_CacheMisses=0;
char* cache_policy;
int main(int argc, char** argv){
	FILE* tracer_file;
	if (argc < 6) throwError();
	if (isValidSize(atoi(argv[1]))  == 1)
		cache_size = atoi(argv[1]);
	else throwError();
	if (isValidSize(atoi(argv[2])) == 1)
		block_size = atoi(argv[2]);
	else throwError();
	if (strcmp(argv[3], "fifo") == 0 || strcmp(argv[3], "lru") == 0)
		cache_policy = argv[3];
	else throwError();
	if (strcmp(argv[4], "direct") == 0)
		associativity = 1;
	else if (strcmp(argv[4], "assoc") == 0)
		associativity = cache_size/block_size;
	else { 
		int n = -1;
		sscanf(argv[4], "assoc:%d", &n);
		if (n == -1 || isValidSize(n) == 0) throwError();
		associativity = n;
	}
	if (atoi(argv[5]) >= 0)
		prefetch_size = atoi(argv[5]);
	else throwError();
	tracer_file = fopen(argv[6], "r");
	if (tracer_file == NULL) throwError();
	sets = cache_size / (block_size * associativity);
	block_bits = log2(block_size);
	index_bits = log2(sets);
	Node** cache = malloc(sets * sizeof(Node*));
	Node** precache = malloc(sets * sizeof(Node*));
	for (int i = 0; i < sets; i++){
		cache[i] = malloc(associativity * sizeof(Node)); 
		precache[i] = malloc(associativity * sizeof(Node));
	}
	for (int i = 0; i < sets; i++)
	for (int j = 0; j < associativity; j++){
		cache[i][j].valid = 0;
		cache[i][j].tag = 0;
		cache[i][j].timestamp = 0;
		precache[i][j].valid = 0;
		precache[i][j].tag = 0;
		precache[i][j].timestamp = 0;
	}
	int hex;
	char operation;
	while (1){
		fscanf(tracer_file, "%c %x\n", &operation, &hex);
		if (operation == '#' || operation == 'e' || operation == 'o' || operation == 'f') break;
		else {
				cache = parse(cache, hex, operation);
				precache = prefetch(precache, hex, operation);	
		} 
	}
printf("no-prefetch\nMemory reads: %d\nMemory writes: %d\nCache hits: %d\nCache misses: %d\n", NP_MemoryReads, NP_MemoryWrites, NP_CacheHits, NP_CacheMisses);
printf("with-prefetch\nMemory reads: %d\nMemory writes: %d\nCache hits: %d\nCache misses: %d\n", P_MemoryReads, P_MemoryWrites, P_CacheHits, P_CacheMisses);
	for (int n = 0; n < sets; n++){
		free(cache[n]);
		free(precache[n]);
	}
	free(cache);
	free(precache);
	return 0;
}
void throwError(){
	printf("error\n");
	exit(0);
}
int isValidSize(int n){
	return ((n > 0) && (((n & (n-1)) == 0)));
}
Node** parse(Node** cached, int address, char op){
		unsigned long tag = (address>>block_bits) >> index_bits;
		unsigned int set = (address>>block_bits) & ((1 << index_bits)-1);
		for (int i = 0; i < associativity; i++){
			if (cached[set][i].tag == tag){
				if (op == 'W')
					NP_MemoryWrites++;
				NP_CacheHits++;
				if (strcmp(cache_policy, "lru") == 0){
					cached[set][i].timestamp = count;
					count++;
				}
				return cached;
			}
		}
	        NP_CacheMisses++;
		NP_MemoryReads++;
		if (op == 'W')
			NP_MemoryWrites++;
		int index = 0, timestamp = cached[set][0].timestamp;
		for (int i = 0; i < associativity; i++){
			if (cached[set][i].valid == 0){ 
				cached[set][i].valid = 1;
				cached[set][i].tag = tag;
				cached[set][i].timestamp = count;
				count++;
				return cached;			
			} 
			if (cached[set][i].timestamp < timestamp){
				timestamp = cached[set][i].timestamp;
				index = i;
			}
 		}
		cached[set][index].tag = tag;
		cached[set][index].timestamp = count;
		count++;
	return cached;
}
Node** prefetch(Node** cached, int address, char op){ 
		unsigned long tag = (address>>block_bits) >> index_bits;
		unsigned int set = (address>>block_bits) & ((1 << index_bits)-1);
		for (int i = 0; i < associativity; i++){
			if (cached[set][i].tag == tag){ //i --> 0
				if (op == 'W')
					P_MemoryWrites++;
				P_CacheHits++;
				if (strcmp(cache_policy, "lru") == 0){
					cached[set][i].timestamp = count; //i --> 0
					count++;
				}
				return cached;
			}
		}	
		P_CacheMisses++;
		P_MemoryReads++;
		if (op == 'W') 
			P_MemoryWrites++;
		int index = 0, wrt = 0, timestamp = cached[set][0].timestamp;
		for (int i = 0; i < associativity; i++){
			if (cached[set][i].valid == 0){ 
				cached[set][i].valid = 1;
				cached[set][i].tag = tag;
				cached[set][i].timestamp = count;
				count++;
				wrt = 1;
				break;
			} 
			if (cached[set][i].timestamp < timestamp){
				timestamp = cached[set][i].timestamp;
				index = i;
			}
 		}
		if (wrt == 0){
			cached[set][index].tag = tag;
			cached[set][index].timestamp = count;
			count++;
		}
	        long prefetch_address = address; 
		int hit;
		for (int i = 0; i < prefetch_size; i++){
			hit = 0;
			prefetch_address = prefetch_address + block_size;
			unsigned int preset = (prefetch_address >> block_bits) & ((1 << index_bits) - 1);
			unsigned long pretag = (prefetch_address >> block_bits) >> index_bits;
			for (int j = 0; j < associativity; j++){
				if (cached[preset][j].tag == pretag)
					hit = 1;
			}
			if (hit == 1) continue;
			P_MemoryReads++;
		     	int index = 0, timestamp = cached[preset][0].timestamp;
		     	for (int f = 0; f < associativity; f++){
				if (cached[preset][f].valid == 0){ 
					cached[preset][f].valid = 1;
					cached[preset][f].tag = pretag;
					cached[preset][f].timestamp = count;
					count++;
					hit = 1;
					break;
				}
				if (cached[preset][f].timestamp < timestamp){
					timestamp = cached[preset][f].timestamp;
					index = f;
				}
 		        }  
		     	if (hit == 1) continue; 
			cached[preset][index].valid = 1;
		     	cached[preset][index].tag = pretag;
			cached[preset][index].timestamp = count;		
			count++;
		}
	return cached;
}
