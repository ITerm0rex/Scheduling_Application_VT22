.PHONY: all clean

all: *.dll

%.dll: *.c
	-gcc -O3 -shared csv.c -o csv.dll
	-gcc -O3 -shared vsv.c -o vsv.dll

*.c:
	-curl -o csv.c --remote-name 'https://www.sqlite.org/src/raw/d14709096280dc0e20c533f184568952bf4b8022ea80afc4aa9fec5ab3637bb3?at=csv.c'
	-curl -o vsv.c --remote-name 'http://www.dessus.com/files/vsv.c'

clean:
	-$(RM) csv.*
	-$(RM) vsv.*