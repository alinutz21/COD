package org.firstinspires.ftc.teamcode.COD;

public class ValoriFunctii {
    /// INTAKE

    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double EXT_HOME = 0.73; // pozitia lui normala
    public final double EXT_EXTENDED = 1; // pozitia lui cand arunca piesa
    public final double DMP_INTAKE_SIDE = 0.3;
    public final double DMP_SCORING_SIDE = 0.7;

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double INTAKE_TIME = 2;

    /// OUTAKE

    /*
     *  VALORILE PENTRU BRATUL DE RIDICARE
     */
    public final int LIFT_DOWN = 0; // pozitia de jos
    public final int LIFT_UP = 500; // pozitia de sus
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double DEPOSIT_IDLE = 0; // pozitia lui normala
    public final double DEPOSIT_SCORING = 0.3; // pozitia lui cand arunca piesa
    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double DUMP_TIME = 3;

}
