#include "timer.h"
#include "i8254.h"
#include "macros.h"
#include "kbd.h"
#include "utilities.h"
#include "bitmap.h"
#include <minix/syslib.h>
#include <minix/drivers.h>


#define gameTime  5;

static counterPlayer1 = 60*gameTime;
static counterPlayer2 = 60*gameTime;

int game_management(){

	drawBoard();

	GAME_STATE game_state = WHITE2PLAY;

	int kbd_hook = KBD_IRQ;

	if(kbd_subscribe_int(&kbd_hook)<0)
		return 1;

	int timer_hook= timer_subscribe_int();
	if(timer_subscribe_int()<0)
		return 1;

	int r, ipc_status;

	unsigned long key = 0;

	message msg;

	int counterPlayer1_tics = counterPlayer1*60;
	int counterPlayer2_tics = counterPlayer2*60;

	int x1 = 5;
	int width = 190;
	int heigth = 30;

	int xPlayer1 = x1+width;
	int xPlayer2 = xPlayer1;
	int yPlayer1 = 250;
	int yPlayer2 = 450;


	//while(gameON);

	if(game_state == WHITE2PLAY){

		float ratio =  (float)width/(float)counterPlayer1; //This might not been working

		while((counterPlayer1 > 0) && (key != KEY_SPACE))
		{
			if ( driver_receive(ANY, &msg, &ipc_status) != 0 ) {
				printf("Driver_receive failed\n");
				continue;
			}
			if (is_ipc_notify(ipc_status)) // Notification received
			{
				switch (_ENDPOINT_P(msg.m_source)) // Notification interrupted
				{
				case HARDWARE:
					if (msg.NOTIFY_ARG & BIT(KBD_IRQ))
					{
						if(sys_inb(KBD_OUT_BUF, &key)!= OK)
							return 1;
					}
					if (msg.NOTIFY_ARG & timer_hook) {
						counterPlayer1_tics-=1;
						if(counterPlayer1_tics%60==0){
							draw_rectangle(x1, x1 + width, yPlayer1, yPlayer1+heigth, 0);
							draw_rectangle(x1, xPlayer1, yPlayer1, yPlayer1+heigth, 26);
							draw_rectangle(x1, xPlayer2, yPlayer2, yPlayer2+heigth,26);
							xPlayer1 -= ratio;
							counterPlayer1-=1;
						}

					}
					break;
				default:
					break; // no other notifications expected: do nothing
				}
			}
		}
	}
	else if(game_state == BLACK2PLAY){

		float ratio =  (float)width/(float)counterPlayer2; //This might not been working
		xPlayer2=x1 + width;

		while((counterPlayer2 > 0) && (key != KEY_SPACE))
		{
			if ( driver_receive(ANY, &msg, &ipc_status) != 0 ) {
				printf("Driver_receive failed\n");
				continue;
			}
			if (is_ipc_notify(ipc_status)) // Notification received
			{
				switch (_ENDPOINT_P(msg.m_source)) // Notification interrupted
				{
				case HARDWARE:
					if (msg.NOTIFY_ARG & BIT(KBD_IRQ))
					{
						if(sys_inb(KBD_OUT_BUF, &key)!= OK)
							return 1;
					}
					if (msg.NOTIFY_ARG & timer_hook) {
						counterPlayer2_tics-=1;
						if(counterPlayer2_tics%60==0){
							draw_rectangle(x1, x1 + width, yPlayer2, yPlayer2+heigth, 0);
							draw_rectangle(x1, xPlayer2, yPlayer2, yPlayer2+heigth, 26);
							draw_rectangle(x1, xPlayer1, yPlayer1, yPlayer1+heigth, 26);
							xPlayer2 -= ratio;
							counterPlayer2-=1;
						}
					}
					break;
				default:
					break; // no other notifications expected: do nothing
				}
			}
		}
	}

	if(kbd_unsubscribe_int(kbd_hook) != 0)
		return 1;

	timer_unsubscribe_int();

	return 0;

}


//	int draw_timer(unsigned int counter, unsigned int color){
//
//		int timer_hook= timer_subscribe_int();
//
//		if(timer_subscribe_int()<0)
//			return 1;
//
//		int r, ipc_status;
//
//		message msg;
//
//
//		int counterPlayer1_tics = counterPlayer1*60;
//
//
//
//		while((counterPlayer1 > 0))
//
//		{
//			if ( driver_receive(ANY, &msg, &ipc_status) != 0 ) {
//
//				printf("Driver_receive failed\n");
//
//				continue;
//
//			}
//
//			if (is_ipc_notify(ipc_status)) // Notification received
//
//			{
//
//				switch (_ENDPOINT_P(msg.m_source)) // Notification interrupted
//
//				{
//
//				case HARDWARE:
//
//					if (msg.NOTIFY_ARG & timer_hook) {
//
//						counterPlayer1_tics-=1;
//
//						if(counterPlayer1_tics%60==0){
//							counterPlayer1-=1;
//							fill_screen(0);
//							draw_rectangle(x1, x2, y1, y2, color);
//							y1 += 1;
//						}
//
//						printf("tempo: %d \n", counterPlayer1);
//					}
//
//					break;
//				default:
//
//					break; // no other notifications expected: do nothing
//
//				}
//
//			}
//
//		}
//
//		timer_unsubscribe_int();
//
//		return 0;
//
//	}
