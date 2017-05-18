/*
 * Menu.h
 *
 *  Created on: Mar 29, 2017
 *      Author: root
 */

#ifndef MENU_CPP_
#define MENU_CPP_

#include <iostream>

using namespace std;

void ClearScreen() {

#ifdef __unix__                    /* __unix__ is usually defined by compilers targeting Unix systems */

	system("clear");

#elif defined(_WIN32) || defined(WIN32)     /* _Win32 is usually defined by compilers targeting 32 or   64 bit Windows systems */

	system("cls");

#endif

}

void showClients(SuperMarketChain S){
	for (unsigned int i=0; i< S.getClients().size();i++)
		cout << i+1 << ". " << S.getClients().at(i)->getName() << endl;
	getchar();
}

void showSupermarkets(SuperMarketChain S){
	for (unsigned int i=0; i< S.getSupermarkets()->size();i++)
		cout << i+1 << ". " << S.getSupermarkets()->at(i)->getName() << endl;
	getchar();
}

void showUnreachable(SuperMarketChain S){
	for(unsigned int i=0; i< S.getUnreachableClients().size();i++)
		cout << i+1 << ". " << S.getUnreachableClients().at(i)->getName() << endl;
	getchar();
}

void showUseless(SuperMarketChain S){
	for(unsigned int i=0; i< S.getUnneededSupermarkets().size();i++)
			cout << i+1 << ". " << S.getUnneededSupermarkets().at(i)->getName() << endl;
	getchar();
}

void searchSupermarkets(SuperMarketChain S){
	string r1, r2;
	cout << "Insert the first road's name: ";
	getline(cin, r1);
	cout << "Insert the second road's name: ";
	getline(cin, r2);

	S.exactSearch(r1, r2);

	getchar();
}

bool mainOptions() {
	int option;
	SuperMarketChain S = SuperMarketChain();

	while(1){

		ClearScreen();
		cout << "Menu: " << endl;
		cout << endl;
		cout << "   " << "1 - See the Graph" << endl;
		cout << "   " << "2 - Show Routes" << endl;
		cout << "   " << "3 - Study Routes" << endl;
		cout << "   " << "4 - See Clients" << endl;
		cout << "   " << "5 - See Supermarkets" << endl;
		cout << "   " << "6 - Show Strongly connected components" << endl;
//		cout << "   " << "7 - Show unreachable Clients" << endl;
		cout << "   " << "7 - Show useless Supermarkets" << endl;
		cout << "   " << "8 - Search for a supermarket" << endl;
		cout << "   " << "9 - Leave" << endl << endl;
		cout << "   " << "Choose your option: ";
		string str_option;
		getline(cin, str_option);
		option = stoi(str_option);

		switch (option){
		case 1:{
			S.displayGraph();
			break;}
		case 2:{
			S.displayRoutes();
			break;}
		case 3:{
			S.studyRoutes();
			break;}
		case 4:{
			showClients(S);
			break;}
		case 5:{
			showSupermarkets(S);
			break;}
		case 6:{
			S.displaySCC();
			break;}
		case 7:{
			showUseless(S);
			break;}
		case 8:{
			searchSupermarkets(S);
			break;}
		case 9:{
			return true;}
		default:{
			continue;}
		}
	}
}

#endif /* MENU_CPP_ */
