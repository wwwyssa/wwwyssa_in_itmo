public class Lab1 {
    static{
        double x = 0/0;
        System.out.println(x);
    }
    public static double firstOperation(double x){
        double firstPart = Math.pow(((Math.pow(x / 2, x)) / 2), 3);
        double secondPart = Math.asin(Math.exp(-Math.abs(x))) - 1;
        return Math.pow(firstPart * secondPart, 2);
    }

    public static double secondOperation(double x){
        double firstPart = Math.pow(x / (x - 0.25), x);
        double secondPart = 1 - Math.pow(x / (x - 1 / 3.0), x);
        double thirdPart = Math.pow(Math.sin(x), firstPart / secondPart);
        thirdPart = thirdPart / 2;
        double fourthPart = Math.cbrt(Math.pow(x, 2.0 / 3.0 / x));
        return Math.pow(thirdPart, fourthPart);
    }

    public static double thirdOperation(double x){
        double firstPart = Math.cos(Math.pow(x, (Math.PI + x) / x)) / Math.PI;
        double secondPart = Math.log(Math.pow(Math.cos(x), 2));
        return 2 * (Math.pow(firstPart, secondPart) - 3);
    }

    public static void print(double[][] z){
        for(double [] tmp : z){
            for(double c : tmp){
                System.out.printf("%10.4f ", c);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        short[] w = new short[7];

        for (short i = 0; i < 7; i+= 1) {
            w[i] = (short) ((i) * 2 + 5);
        }
        double[] x = new double[12];
        for (short i = 0; i < 12; i++) {
            x[i] = -3 + Math.random() * 17;
        }
        double[][] z = new double[7][12];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 12; j++) {
                if (w[i] == 5.0){
                    z[i][j] = firstOperation(x[j]);
                } else if (w[i] == 7.0 || w[i] == 11.0 || w[i] == 15.0) {
                    z[i][j] = secondOperation(x[j]);
                } else{
                    z[i][j] = thirdOperation(x[j]);
                }
            }
        }
        print(z);
    }
}