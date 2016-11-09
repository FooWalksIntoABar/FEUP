#ifndef DATA_H_
#define DATA_H_

class Data{
 unsigned int dia;
 unsigned int mes;
 unsigned int ano;

public:
  Data(unsigned int dia, unsigned int mes, unsigned int ano);
  bool operator == (Data & rhs);
  bool operator < (Data & rhs); // Servirá para ordenar com o QuickSort
  bool operator += (int n);
};

#endif /* DATA_H_ */
