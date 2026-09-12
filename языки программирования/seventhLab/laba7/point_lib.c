#include <stdio.h>
#include <math.h>

typedef struct Point {
    int x;
    int y;
} Point;

void process_points(Point* points, int num_points, double* distances)  {

    int num_pairs = num_points / 2;

    printf("C: точки получены\n");

    for (int i = 0; i < num_pairs; i++) {
        Point *p1 = &points[i * 2];
        Point *p2 = &points[i * 2 + 1];

        double dx = (double)p2->x - (double)p1->x;
        double dy = (double)p2->y - (double)p1->y;
        double distance = sqrt(dx * dx + dy * dy);
        distances[i] = distance;
    }
    printf("C: обработка завершена\n");
}

void process_point(Point * p)  {
    printf("Point recevied x: %d, y: %d", p->x, p->y);
    p->x = 10;
    p->y = 10;

}

void process(Point* p)  {
    printf("Point recevied x: %d, y: %d", p->x, p->y);
    p->x = 10;
    p->y = 10;

}