#include <stdio.h>
#include <stdlib.h>
// Структура, содеражащая динамический массив структур.
// В структурах разные типы - алоцируемые, enum, структуры
// Методы дял работы с этой структурой:
// Вывод содержимого (либо в консоль либо в поток)
// Добавление в структуру
// Удаление
// Размер
// Нет утечек памяти !!
//
//

//for(size_t i = 0; i < v->size; ++i){
//    printf("Unit %s: pos(%d, %d), health: %d, type: %d\n", v->units[i].name, v->units[i].position.x, v->units[i].position.y, v->units[i].health, v->units[i].type);
//  }
typedef struct{
  int x;
  int y;
} Point;

typedef enum{
  Ctype = 0,
  Btype = 1,
  Atype = 2
} UnitType;

typedef struct{
  char * name;
  Point position;
  int health;
  UnitType type;
  
} Unit;


typedef struct {
  Unit* units;
  size_t size;
} Vec;

void Print(Vec * v){
  for(size_t i = 0; i < v->size; i++){
      printf("Unit %s\npos:x=%d, y=%d\nhealth=%d\ntype=%d\n=========\n", v->units[i].name, v->units[i].position.x, v->units[i].position.y, v->units[i].health, v->units[i].type);
  }
}

char RemoveUnit(Vec* v, int idx){
  if (idx >= v->size || idx < 0){
    return -1;
  }
  Unit *tmp = malloc((v->size - 1) * sizeof(Unit));
  if (tmp == NULL){
    return -1;
  }
  for(int i = 0; i < idx; i++){
    tmp[i] = v->units[i];
  }
  for(int i = idx + 1; i < v->size; i++){
    tmp[i-1] = v->units[i];
  }
  free(v->units);
  v->units = tmp;
  v->size -= 1;
  return 1;

}


char AddNewUnit(Vec * v, Unit * u){
  if (v->size == 0){
    v->units = malloc(1 * sizeof(Unit));
    if (v->units == NULL){
      return -1;
    }
    v->units[v->size] = *u;
    v->size++; 
    return 1;
  }
  Unit *tmp = realloc(v->units, (v->size + 1) * sizeof(Unit));
  if (tmp == NULL){
    return -1;
  }
  if (tmp == NULL){
    return -1;
  }

  v->units = tmp;
  v->units[v->size] = *u;
  v->size++;
}




int main() {

  Vec * v = malloc(sizeof(Vec));

  v->size = 0;
  v->units = NULL;

  Unit a = {"A", {1, 2}, 100, Ctype};

  char flag = AddNewUnit(v, &a);

  Unit b = {"B", {2, 2}, 200, Btype};

  char flag2 = AddNewUnit(v, &b);

  Unit c = {"C", {2, 2}, 300, Atype};

  char flag3 = AddNewUnit(v, &c);

  if (flag == -1 || flag2 == -1){
    printf("Error ");
  }

  Print(v);

  char removeFlag = RemoveUnit(v, 1);
  if (removeFlag == -1){
    printf("REMOVE ERROR");
    return 1;
  }
  printf("----------------------REMOVE-------------\n");
  Print(v);
  free(v->units);
  free(v);
  
  return 0;
}