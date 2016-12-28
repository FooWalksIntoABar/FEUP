#pragma once

#define BIT(n) (0x01<<n)
#define singleBit(byte) (0x01 & byte)
#define swap(type, i, j) {type t = i; i=j; j=t;}

#define ROWS 8
#define COLS 8

#define CORRECTION 25
#define SQUARE_SIZE	37.5

#define YELLOW 				65024
#define RED					38914
#define BLUE				255
#define WHITE				16777215
#define BLACK				0

#define VIDEO_MEM 	  				1
#define SECOND_BUFFER 				2
#define THIRD_BUFFER  				3








int fileExists(const char* filename);
