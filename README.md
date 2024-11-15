# Rubix Team Season 9 - "Into The Deep" - README

## Descriere generală

Bine ați venit la documentația oficială a echipei Rubix pentru sezonul 9 - "Into The Deep". Acest proiect are ca scop dezvoltarea unui robot autonom și eficient pentru competițiile de robotică FIRST Tech Challenge (FTC). Sezonul 9 aduce mai multe îmbunătățiri semnificative față de edițiile anterioare, inclusiv integrarea de tehnici avansate de machine learning, odometrie nouă, și un sistem bazat pe comenzi de tip machine-based.

În acest fișier README, vom descrie arhitectura codului, componentele de pe Control Hub, atribuțiile driverilor și îmbunătățirile implementate în acest sezon.

---

## Arhitectura echipei

### Driveri

În echipa Rubix, atribuțiile driverilor sunt împărțite în funcție de zonele cheie ale controlului robotului. Fiecare driver este responsabil de o anumită secțiune a operațiunilor robotului pentru a asigura o manevrabilitate eficientă și un control fluid în timpul competiției.

1. **Driver 1 - Controlul mișcării robotului**  
   - **Responsabilități principale:**
     - Controlul mișcării robotului prin telemetrie (joystickuri).
     - Supravegherea modului de deplasare al robotului, inclusiv direcția și viteza.
     - Implementarea și monitorizarea sistemului de odometrie pentru a asigura precizia mișcărilor pe teren.
   
2. **Driver 2 - Controlul brațului și al mecanismelor de manipulare**  
   - **Responsabilități principale:**
     - Controlul și coordonarea mișcărilor brațului robotic.
     - Activarea și controlul mecanismelor de manipulare a obiectelor (prindere, ridicare, plasare).
     - Implementarea unor strategii de manipulare avansate bazate pe feedback-ul de la senzori (de exemplu, distanță sau proximitate).

3. **Strategist**  
   - **Responsabilități principale:**
     - Planificarea mișcărilor și strategiilor de joc în timpul meciurilor.
     - Analiza în timp real a activităților robotului și ajustarea acestora pentru a îndeplini obiectivele jocului.
     - Coordonarea cu ceilalți membri ai echipei pentru a maximiza eficiența performanței în timpul competițiilor.

---

## Componentele robotului

### Control Hub

Control Hub-ul este creierul robotului Rubix și conține toate componentele esențiale pentru managementul operațiunilor robotului. Iată o listă a principalelor componente și ce funcționalități aduc:

1. **Control Hub - Hardware principal**
   - Este unitatea centrală care găzduiește procesorul principal al robotului.
   - Conține un microprocesor dedicat, memorie RAM, și stocare pentru salvarea codului și a datelor de la senzori.
   - Gestionează comunicațiile între diferitele componente și modulele robotului.
   
2. **Motors (Motoare)**
   - Motoarele sunt conectate la Control Hub prin intermediul unor controlere de motor (Motor Controllers).
   - Acestea sunt responsabile pentru mișcarea robotului (roți, brațe, etc.).
     
3. **Servo Motors (Servomotoare)**
   - Acestea sunt utilizate pentru mișcarea precisă a anumitor părți ale robotului, cum ar fi brațul sau mecanismele de prindere.

---

## Îmbunătățiri aduse sezonului 9

Sezonul 9 al echipei Rubix aduce mai multe îmbunătățiri importante care vor contribui la performanța robotului în competiții. Acestea includ noi tehnologii și metode de control, precum și îmbunătățiri ale sistemului de navigare și manipulare a obiectelor.

### 1. **Machine Learning**

Pentru acest sezon, am integrat algoritmi de machine learning în strategia de control a robotului. Acestea sunt folosite pentru a îmbunătăți identificarea obiectelor și optimizarea deciziilor robotului în funcție de mediul său. Algoritmul de învățare automată este antrenat pentru a recunoaște diverse tipuri de obiecte, a evalua starea acestora și a lua decizii de manipulare într-un mod mai inteligent și mai precis.

- **Aplicabilitate:**
  - Recunoașterea obiectelor de pe teren.
  - Estimarea distanței și direcției corecte pentru a ajunge la un obiect.
  - Ajustarea strategiilor de manipulare în funcție de feedback-ul continuu.

### 2. **Noua metodă de odometrie**

Sezonul 9 aduce o îmbunătățire semnificativă în ceea ce privește sistemul de odometrie al robotului. În loc de un sistem tradițional bazat pe roți, am implementat un sistem de odometrie vizuală, care combină informațiile din senzorii robotului (în special gyroscopul și senzorii de distanță) cu datele vizuale de la cameră pentru a calcula mai precis poziția robotului pe teren.

- **Avantaje:**
  - Posibilitatea de a urmări mișcările robotului cu o mai mare precizie.
  - Corectarea automată a abaterilor de la traseu datorită feedback-ului vizual.
  - Reducerea erorilor cauzate de alunecarea roților sau deformarea terenului.

### 3. **Machine-based Command System**

Un alt element cheie al robotului Rubix în acest sezon este implementarea unui **sistem bazat pe comenzi de tip machine-based**, care permite robotului să ia decizii autonome într-un mod mai eficient. Acest sistem combină logica bazată pe reguli și învățarea automată pentru a lua decizii rapide și exacte în timpul meciurilor.

- **Aplicabilitate:**
  - Luarea deciziilor pentru a evita obstacolele pe traseu.
  - Alegerea celei mai bune rute pentru a ajunge la obiectivele dorite pe teren.
  - Adaptarea la condițiile în schimbare în timpul meciurilor.

### 4. **Optimizarea manipulării obiectelor**

Am îmbunătățit sistemul de manipulare a obiectelor prin implementarea unui mecanism mai rapid și mai precis de prindere. Acesta folosește feedback-ul senzorilor de presiune și de distanță pentru a ajusta forța și poziția brațului robotic în timp real, asigurând o manipulare mai bună a obiectelor de pe teren.

### 5. **Algoritmi avansați de navigare**

Un alt aspect important al îmbunătățirilor aduse este implementarea unor algoritmi avansați de navigare, care utilizează datele de la odometrie și senzori pentru a planifica traseele și pentru a naviga mai eficient prin terenul competiției.

---

## Concluzie

Echipa Rubix pentru sezonul 9 - "Into The Deep" se află la frontiera inovației și tehnologiei în domeniul roboticii FTC. Prin integrarea de machine learning, odometrie avansată și un sistem bazat pe comenzi de tip machine-based, robotul nostru devine mai autonom, mai rapid și mai precis decât niciodată. Suntem pregătiți să facem față provocărilor sezonului și să aducem noi realizări pe teren.
