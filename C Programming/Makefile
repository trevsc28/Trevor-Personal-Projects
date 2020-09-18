all: HousingPrices

HousingPrices: HousingPrices.c
	gcc -Wall -Werror -fsanitize=address -std=c11 HousingPrices.c -o HousingPrices

clean:
	rm -f HousingPrices
