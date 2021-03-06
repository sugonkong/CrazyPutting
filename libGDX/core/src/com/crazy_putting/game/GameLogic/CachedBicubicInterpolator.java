package com.crazy_putting.game.GameLogic;

public class CachedBicubicInterpolator
{
    private double a00, a01, a02, a03;
    private double a10, a11, a12, a13;
    private double a20, a21, a22, a23;
    private double a30, a31, a32, a33;

    private double minX;
    private double maxX;
    public void updateCoefficients (double[][] p) {
        a00 = p[1][1];
        a01 = -.5*p[1][0] + .5*p[1][2];
        a02 = p[1][0] - 2.5*p[1][1] + 2*p[1][2] - .5*p[1][3];
        a03 = -.5*p[1][0] + 1.5*p[1][1] - 1.5*p[1][2] + .5*p[1][3];
        a10 = -.5*p[0][1] + .5*p[2][1];
        a11 = .25*p[0][0] - .25*p[0][2] - .25*p[2][0] + .25*p[2][2];
        a12 = -.5*p[0][0] + 1.25*p[0][1] - p[0][2] + .25*p[0][3] + .5*p[2][0] - 1.25*p[2][1] + p[2][2] - .25*p[2][3];
        a13 = .25*p[0][0] - .75*p[0][1] + .75*p[0][2] - .25*p[0][3] - .25*p[2][0] + .75*p[2][1] - .75*p[2][2] + .25*p[2][3];
        a20 = p[0][1] - 2.5*p[1][1] + 2*p[2][1] - .5*p[3][1];
        a21 = -.5*p[0][0] + .5*p[0][2] + 1.25*p[1][0] - 1.25*p[1][2] - p[2][0] + p[2][2] + .25*p[3][0] - .25*p[3][2];
        a22 = p[0][0] - 2.5*p[0][1] + 2*p[0][2] - .5*p[0][3] - 2.5*p[1][0] + 6.25*p[1][1] - 5*p[1][2] + 1.25*p[1][3] + 2*p[2][0] - 5*p[2][1] + 4*p[2][2] - p[2][3] - .5*p[3][0] + 1.25*p[3][1] - p[3][2] + .25*p[3][3];
        a23 = -.5*p[0][0] + 1.5*p[0][1] - 1.5*p[0][2] + .5*p[0][3] + 1.25*p[1][0] - 3.75*p[1][1] + 3.75*p[1][2] - 1.25*p[1][3] - p[2][0] + 3*p[2][1] - 3*p[2][2] + p[2][3] + .25*p[3][0] - .75*p[3][1] + .75*p[3][2] - .25*p[3][3];
        a30 = -.5*p[0][1] + 1.5*p[1][1] - 1.5*p[2][1] + .5*p[3][1];
        a31 = .25*p[0][0] - .25*p[0][2] - .75*p[1][0] + .75*p[1][2] + .75*p[2][0] - .75*p[2][2] - .25*p[3][0] + .25*p[3][2];
        a32 = -.5*p[0][0] + 1.25*p[0][1] - p[0][2] + .25*p[0][3] + 1.5*p[1][0] - 3.75*p[1][1] + 3*p[1][2] - .75*p[1][3] - 1.5*p[2][0] + 3.75*p[2][1] - 3*p[2][2] + .75*p[2][3] + .5*p[3][0] - 1.25*p[3][1] + p[3][2] - .25*p[3][3];
        a33 = .25*p[0][0] - .75*p[0][1] + .75*p[0][2] - .25*p[0][3] - .75*p[1][0] + 2.25*p[1][1] - 2.25*p[1][2] + .75*p[1][3] + .75*p[2][0] - 2.25*p[2][1] + 2.25*p[2][2] - .75*p[2][3] - .25*p[3][0] + .75*p[3][1] - .75*p[3][2] + .25*p[3][3];
    }


    public static double getValue (double[] p, double x) {
        return p[1] + 0.5 * x*(p[2] - p[0] + x*(2.0*p[0] - 5.0*p[1] + 4.0*p[2] - p[3] + x*(3.0*(p[1] - p[2]) + p[3] - p[0])));
    }
   private double[] arr = new double[4];
    public float getValuefast (double x, double y) {
        double x2 = x * x;
        double x3 = x2 * x;
        double y2 = y * y;
        double y3 = y2 * y;

        return (float)((a00 + a01 * y + a02 * y2 + a03 * y3) +
                (a10 + a11 * y + a12 * y2 + a13 * y3) * x +
                (a20 + a21 * y + a22 * y2 + a23 * y3) * x2 +
                (a30 + a31 * y + a32 * y2 + a33 * y3) * x3);
    }
//    public float getValueSlow (double[][] p, double x, double y) {
//       // y +=.5f;
//        arr[0] = getValue(p[0], x);
//        arr[1] = getValue(p[1], x);
//        arr[2] = getValue(p[2], x);
//        arr[3] = getValue(p[3], x);
//       //    System.out.println(y+"Y");
//        return (float)arr[3];
//      //  x +=.5f;
//        //return (float)getValue(arr, y);
//    }
    public float getValueSlow (double[][] p, double x, double y) {
        y += .5f;
        x += .5f;
        if(minX > y) minX = y;
        if(maxX < y) maxX = y;

        System.out.println("MIN X "+minX+ "Max X "+ maxX);

        arr[0] = getValue(p[0], x);
        arr[1] = getValue(p[1], x);
        arr[2] = getValue(p[2], x);
        arr[3] = getValue(p[3], x);
        System.out.println(x+"Y");
        return (float) arr[3];
       // return (float)getValue(arr, y);
    }

}
