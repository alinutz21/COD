package org.firstinspires.ftc.teamcode.COD;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ValoriFunctii {
    /// INTAKE

    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double EXT_HOME = 0.73; // pozitia lui normala
    public final double EXT_EXTENDED = 1; // pozitia lui cand este extins
    public final double DMP_INTAKE_SIDE = 0;
    public final double DMP_SCORING_SIDE = 0.95;

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double INTAKE_TIME = 4;

    /// OUTAKE

    /*
     *  VALORILE PENTRU BRATUL DE RIDICARE
     */
    public final int LIFT_DOWN = 1; // pozitia de jos
    public static final int LIFT_UP = 50; // pozitia de sus
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double DEPOSIT_IDLE = 0; // pozitia lui normala
    public final double DEPOSIT_SCORING = 0.3; // pozitia lui cand arunca piesa
    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double DUMP_TIME = 7;

}
