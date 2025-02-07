package org.firstinspires.ftc.teamcode.COD;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ValoriFunctii {
    /// INTAKE
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double EXT_HOME = 0.67; // pozitia lui normala
    public final double EXT_EXTENDED = 0.985; // pozitia lui cand este extins
    public final double DMP_INTAKE_SIDE = 0.06;
    public final double DMP_HORIZONTAL_SIDE = 0.8;
    public final double DMP_SCORING_SIDE = 0.965;
    public final double DMP_90DEGREES = 0.35;

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double RETURN_TIME = 1.5;
    public final double SAMPLE_OUT_TIME = 1.5;

    /// OUTAKE

    /*
     *  VALORILE PENTRU BRATUL DE RIDICARE
     */


    public final double VITEZA_MAXIMA_URCARE = 0.5;
    public final double VITEZA_MAXIMA_COBORARE = 0.3;
    public final double LIFT_DOWN = 0.5; // pozitia de jos
    public final double LIFT_BASKET1 = 50; // pozitia de sus
    public final double LIFT_BASKET2 = 86;
    public final double LIFT_SPECIMEN1 = 30; // pozitia de sus
    public final double LIFT_SPECIMEN2 = 60; // pozitia de sus
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double DEPOSIT_IDLE = 1; // pozitia lui normala   --   0.932
    public final double DEPOSIT_SCORING = 0.5; // pozitia lui cand arunca piesa
    public final double DEPOZIT_HORIZONTAL = 0.9;
    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double DUMP_BASKET_TIME = 1.5;
    public final double DUMP_SPECIMEN_TIME = 5;
    public final double SPECIMEN_OPEN = 0.36;//0.43
    public final double SPECIMEN_CLOSED = 0.41;

}
