.PHONY: all clean

all:
	-gcc -O3 -shared csv.c -o csv.dll
	-gcc -O3 -shared vsv.c -o vsv.dll

clean:
	-$(RM) csv.dll
	-$(RM) vsv.dll