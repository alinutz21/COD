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
    public final double DMP_INTAKE_SIDE = 0.03;
    public final double DMP_SCORING_SIDE = 0.95;

    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double INTAKE_TIME = 2;

    /// OUTAKE

    /*
     *  VALORILE PENTRU BRATUL DE RIDICARE
     */


    public final double VITEZA_MAXIMA_URCARE = 0.5;
    public final double VITEZA_MAXIMA_COBORARE = 0.3;
    public final int LIFT_DOWN = 1; // pozitia de jos
    public final int LIFT_BASKET1 = 50; // pozitia de sus
    public final int LIFT_BASKET2 = 70;
    public final int LIFT_SPECIMEN1 = 30; // pozitia de sus
    public final int LIFT_SPECIMEN2 = 60; // pozitia de sus
    /*
     *   VALORILE PENTRU SERVO-UL CARE DEPOZITEAZA
     */
    public final double DEPOSIT_IDLE = 0.36; // pozitia lui normala
    public final double DEPOSIT_SCORING = 0.19; // pozitia lui cand arunca piesa
    // TIMPUL ALOCAT PENTRU CA SERVO-UL SA PUNA PIESA IN COS
    public final double DUMP_BASKET_TIME = 5;
    public final double DUMP_SPECIMEN_TIME = 5;
    public final double SPECIMEN_OPEN = 0.9;
    public final double SPECIMEN_CLOSED = 0.5;


}
