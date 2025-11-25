#include <stdio.h>
#include <math.h>

typedef struct Point {
   float x;
   float y;
} Point;

typedef struct PointPair {
    Point p1;
    Point p2;
    double distance;
} PointPair;



void calculate_distances(PointPair* pairs, int count) {
    for (int i = 0; i < count; i++) {
        int dx = pairs[i].p2.x - pairs[i].p1.x;
        int dy = pairs[i].p2.y - pairs[i].p1.y;
        pairs[i].distance = sqrt(dx * dx + dy * dy);
    }
}