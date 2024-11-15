package org.firstinspires.ftc.teamcode.COD.Subsystems;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.util.Range;



public class Slide
{
    // Constante pentru caracterizarea alunecării (Slide characterization).

    // KP specifică coeficientul proporțional al regulatorului PID din PIDF.

    private static final double KP = 0;

    // KI specifică coeficientul integral al regulatorului PID din PIDF.
    private static final double KI = 0;

    // KD specifică coeficientul derivativ al regulatorului PID din PIDF.

    private static final double KD = 0;

    // KF specifică puterea de menținere a alunecării la orice înălțime.

    private static final double KF = 0;

    // ZERO_OFFSET specifică lungimea extensiei alunecării în inci în poziția de repaus (de obicei 0.0).
    private static final double ZERO_OFFSET = 0.0;

    // MIN_POS specifică lungimea extensiei alunecării în inci în poziția sa minimă (de obicei aceeași cu ZERO_OFFSET).
    private static final double MIN_POS = ZERO_OFFSET;

    // MAX_POS specifică lungimea extensiei alunecării în inci în poziția sa maximă.
    private static final double MAX_POS = 39.37;

    // TICKS_PER_INCH specifică factorul de scalare pentru a traduce lungimea extensiei alunecării din inci în tickeți ai encoderului.
    private static final double TICKS_PER_INCH = 537.7;

    // Componentele sub-sistemului de alunecare.
    private final DcMotorEx leftMotor;
    private final PIDController pidController;

    // Stările alunecării.
    private double targetPosInInches;
    private double revPowerLimit, fwdPowerLimit;

    /**
     * Constructor: creează și initializează tot ce este necesar pentru sub-sistemul de alunecare, inclusiv motoarele de alunecare și
     * regulatorul PID.
     *
     * @param hardwareMap specifică hardwareMap pentru a accesa obiectul motorului.
     * @param leftMotorName specifică numele hardware al motorului stâng.
     * @param leftMotorInverted specifică true pentru a inversa direcția motorului stâng, false în caz contrar.
     * @param brakeEnabled specifică true pentru a activa modul de frână al motorului, false pentru a activa modul de coastă.

     */
    public Slide(
            HardwareMap hardwareMap, String leftMotorName, boolean leftMotorInverted, boolean brakeEnabled)
    {
        // Creează motoarele de alunecare și le initializează.
        leftMotor = hardwareMap.get(DcMotorEx.class, leftMotorName);
        leftMotor.setDirection(leftMotorInverted ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        leftMotor.setZeroPowerBehavior(
                brakeEnabled ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Poate vrei să folosești RUN_USING_ENCODER dacă dorești să faci control al vitezei în loc de OpenLoop.
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        // Creează regulatorul PID pentru alunecare și îl initializează cu coeficientele PID corespunzătoare.
        pidController = new PIDController(KP, KI, KD);

        this.targetPosInInches = ZERO_OFFSET;
        this.revPowerLimit = -0.5;
        this.fwdPowerLimit = 0.7;
    }

    /**
     * Această metodă trebuie apelată periodic pentru a face control PID al alunecării către poziția țintă stabilită
     * și a o menține. Notă: aceasta este echivalentul metodei .update() în Command-Based (de exemplu, FTCLib).
     */
    public void slideUpdate()
    {
        // targetPosInInches, revPowerLimit și fwdPowerLimit sunt setate de metoda setPosition.
        double targetPosInTicks = (targetPosInInches - ZERO_OFFSET) * TICKS_PER_INCH;
        // Este suficient să citim poziția unei singure părți ale alunecării, presupunând că ambele părți sunt legate mecanic.
        double currPosInTicks = leftMotor.getCurrentPosition();
        double pidOutput = pidController.calculate(currPosInTicks, targetPosInTicks);
        // Compensarea gravitației ar trebui să fie o constantă pentru o alunecare.
        double power = pidOutput + KF;
        // Limitează puterea la intervalul dintre revPowerLimit și fwdPowerLimit.
        // Pentru o alunecare/elevatoare, revPowerLimit și fwdPowerLimit pot fi asimetrice (adică revPowerLimit și
        // fwdPowerLimit au magnitudini diferite. De exemplu, putere mai mare pentru urcare decât pentru coborâre).
        power = Range.clip(power, revPowerLimit, fwdPowerLimit);
        leftMotor.setPower(power);
    }

    /**
     * Această metodă este de obicei apelată de autonom pentru a determina când alunecarea a ajuns la țintă, astfel încât să poată
     * trece la următoarea operațiune.
     *
     * @param toleranceInInches specifică toleranța în inci în care vom considera că am ajuns la țintă.
     * @return true dacă alunecarea este pe țintă în limita toleranței, false în caz contrar.
     */
    public boolean isOnTarget(double toleranceInInches)
    {
        double currPosInInches = getPosition();
        return Math.abs(targetPosInInches - currPosInInches) <= toleranceInInches;
    }

    /**
     * Această metodă poate fi apelată de autonom sau teleop pentru a seta ținta alunecării. De obicei, în TeleOp, se poate reacționa
     * la o apăsare de buton și se poate apela această metodă pentru a muta alunecarea într-o poziție prestabilită.
     *
     * @param targetPosInInches specifică poziția țintă în inci.
     * @param powerLimit specifică puterea maximă pentru mișcarea alunecării.
     */
    public void setPosition(double targetPosInInches, double powerLimit)
    {
        this.targetPosInInches = targetPosInInches;
        powerLimit = Math.abs(powerLimit);
        this.revPowerLimit = -powerLimit;
        this.fwdPowerLimit = powerLimit;
    }

    /**
     * Această metodă este de obicei utilizată de TeleOp pentru a controla mișcarea alunecării prin valoarea joystick-ului,
     * astfel încât viteza mișcării alunecării să poată fi controlată de joystick. Deoarece aceasta este controlată prin PID,
     * alunecarea va încetini când se apropie de limitele minime sau maxime, chiar dacă joystick-ul este apăsat până la
     * poziția maximă.
     *
     * @param power specifică puterea maximă pentru mișcarea alunecării.
     */
    public void setPowers(double power)
    {
        if (power > 0.0)
        {
            // Mărește alunecarea către poziția maximă cu puterea specificată.
            setPosition(MAX_POS, power);
        }
        else if (power < 0.0)
        {
            // Mărește alunecarea către poziția minimă cu puterea specificată.

            setPosition(MIN_POS, power);
        }
        else
        {
            // Menține poziția alunecării fără limită de putere.
            setPosition(getPosition(), 1.0);
        }
    }

    /**
     * Această metodă traduce ticheții encoderului în poziția reală a alunecării în inci.
     *
     * @param encoderTicks specifică ticheții encoderului motorului.
     * @return poziția tradusă a alunecării în inci.
     */

    public double ticksToRealWorldInches(double encoderTicks)
    {
        return encoderTicks / TICKS_PER_INCH + ZERO_OFFSET;
    }

    /**
     * Această metodă returnează poziția alunecării în inci în lumea reală.
     *
     * @return poziția alunecării în inci.
     */
    public double getPosition()
    {
        return ticksToRealWorldInches(leftMotor.getCurrentPosition());
    }
}

