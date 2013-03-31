package net.wohlfart.tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.google.caliper.SimpleBenchmark;

public class SimpleMathBenchmarks {

    public static class Benchmark01 extends SimpleBenchmark {
        Quaternion q = new Quaternion();
        Vector3f v = new Vector3f();
        Matrix4f m = new Matrix4f();

        public float timeConvertVector2Matrix(int reps) {
            float dummy = 0;
            for (int i = 0; i < reps; i++) {
                SimpleMath.convert(v, m);
                dummy += m.m00;
            }
            return dummy;
        }

        public float timeConvertQuaternion2Matrix(int reps) {
            float dummy = 0;
            for (int i = 0; i < reps; i++) {
                SimpleMath.convert(q, m);
                dummy += m.m00;
            }
            return dummy;
        }


    }

}
