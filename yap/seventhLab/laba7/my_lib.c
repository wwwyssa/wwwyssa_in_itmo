 #ifdef _WIN32
#define EXPORT __declspec(dllexport)
#else
#define EXPORT
#endif

EXPORT long long fib_c(int n) {
    if (n <= 1) {
        return n;
    } else {
        return fib_c(n - 1) + fib_c(n - 2);
    }
}

EXPORT long long fib_c_iterative(int n) {
    if (n <= 1) return n;
    
    long long a = 0;
    long long b = 1;
    long long temp;
    
    for (int i = 2; i <= n; i++) {
        temp = a + b;
        a = b;
        b = temp;
    }
    return b;
}

#include <stdio.h>

typedef struct Point {
   int x;
   int y;
} Point;

EXPORT void process_point(Point * p)  {
    printf("Point recevied x: %d, y: %d", p->x, p->y);
    p->x = 10;
    p->y = 10;
    
}

// p[0]
// p[1]
EXPORT void process(Point* p)  {
    printf("Point recevied x: %d, y: %d", p->x, p->y);
    p->x = 10;
    p->y = 10;
    
}

EXPORT void process_array_point(Point* p, int count)  {
    for (int i = 0; i < count; i++) {
        printf("Point recevied x: %d, y: %d\n", p[i].x, p[i].y);
        p[i].x = 10;
        p[i].y = 10;
    }
    
}



#include <stdio.h>


typedef int (*FilterFunc)(Point);

EXPORT int filter(Point* arr, int n, FilterFunc func, Point* out)
{
    int k = 0;
    for (int i = 0; i < n; i++)
    {
        if (func(arr[i])) {
            out[k] = arr[i];
            k++;
        }
    }
    return k;
}

EXPORT int q1(Point p) { return p.x > 0 && p.y > 0; }
EXPORT int q2(Point p) { return p.x < 0 && p.y > 0; }
EXPORT int q3(Point p) { return p.x < 0 && p.y < 0; }
EXPORT int q4(Point p) { return p.x > 0 && p.y < 0; }

// Bubble sort Points by x ascending, then y ascending
EXPORT void bubble_sort_points(Point* arr, int n)
{
    if (!arr || n <= 1) return;
    for (int i = 0; i < n - 1; i++) {
        int swapped = 0;
        for (int j = 0; j < n - 1 - i; j++) {
            if (arr[j].x > arr[j + 1].x ||
               (arr[j].x == arr[j + 1].x && arr[j].y > arr[j + 1].y)) {
                Point tmp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = tmp;
                swapped = 1;
            }
        }
        if (!swapped) break;
    }
}
