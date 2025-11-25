#include <math.h>



void pair_distances(const double* data, size_t count, double* out) {
    for (size_t i = 0; i < count; ++i) {
        double x1 = data[i*4];
        double y1 = data[i*4 + 1];
        double x2 = data[i*4 + 2];
        double y2 = data[i*4 + 3];
        double dx = x2 - x1;
        double dy = y2 - y1;
        out[i] = sqrt(dx*dx + dy*dy);
    }
}