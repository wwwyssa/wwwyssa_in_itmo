 long long fib_c(int n) {
    if (n <= 1) {
        return n;
    } else {
        return fib_c(n - 1) + fib_c(n - 2);
    }
}

long long fib_c_iterative(int n) {
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