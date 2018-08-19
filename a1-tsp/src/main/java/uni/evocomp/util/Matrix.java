package uni.evocomp.util;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
WW000Z0MMMMWSZM            .,    ::::. ...,::::::,::,:,,,..       i;;i;S;8MMMMMM0MMMMMMMMMMMMW@@MMMM
WMMMMMMM2r  782  MMMMMX ,     M2     :      ..  .. ..        S BMBa   .MMB MMMS2MMMMMMMZ8MMMMMMMMMMX
@MMMMMMM07  MMM  ;MMMMiWMMMMX MMMMa   @MS   ...,,....     ;MMMMM2@   :77MMaaMZBMMMMMMZ;0MMMMMMMMMM7X
MMM8SSi7a. 0M@7  aMMMM  0MMMMMMM@MMMMMMMMM:              0MMMXW  MZ   ZSrr7;MMMMBMMMZrMMMMMMWMMM0MMB
MMM7MMB:  XMMM;    M02        MMMMMMMMMMMMMMMMMMiMMMMMMMMMMMa  B  ;8MMB8B,   ZBMM8B,WMMMMMM@MMMMSWMZ
MWX7 7:  ,MMMM     MMr    ZM2 MM0ZM@MMMMMMBM:..:.       MMM8  :MZ    M80MMMMMM0S22S7M0;ZMB00MMaMMMMZ
MS      20MW2   MMMMMMMX   M MMMMMM ;SM@MMMM   .,:7SaZX8MMMr MMMMSa  M@MMB8MB8MMWMM  WWMZ82;  XMMMM2
MMMB:   0 SMMr.MMMMMMMMM MMMMMMMMM  MM@.MMMMX         iBMMMB ,MMMMMi MMMMir8B20MMMMMMMMMMa.MM ZMMMWX
ZMM:  MMMr8Z MMMMMMMMMM  .MMM  MM  ;M aMMMWM7 .   ,... @M8SMMBZ2BMMMMMMBW@B  i  MMMM@MMZ;XMMM@BMMM@2
Z0M0XMM  aSSMMMMMMMMMMBiBMMW  MMM, M :ZMM@MB  ,.  .,,,rZ MMMMMMW8SX22aMMMMM  M7 M2  MMr;MM@MMMMM 0Ma
Wa2MMMMMMMMMMMMMMMMMMi  MM8   M7   MMMMMWMMM  ,.  ...,2M MXM@@MMMMMMW82SS0M0MM;:MM  B7r 0MM rMM  .MX
WX  @MMMMMMMMMMMMMMW.  .SM        .M WM0@MM  .,.  ....i0Z8 SS;   28W0a0BBW@MMMMMZM@W@@BMMMMMMMM   Ma
BX   0MMMMMMMMMMMMBBr WMM; . XM8M @0MMWBM@  .,,.  .,,,:SMaMMMMM :@ ;MMMMMMM@WWWMMMM@MMMMMMMZZBM   Ma
0S     BMMMMMMMMMMMMM@MM0    8;   MMMW@MM  ,,,,.  .,,.,SM 2MMMMMMX  0MMBXrBMMMMMMMMMMMM@ZaZZ0MW   M0
ZX...    7MMMMMMMMMMMMMMM       0MMW0MMM  .:,:,.  ..,,,2M@0.   XMMMMMMMMMMMMMMMaWMMMBa2aZ0000M0   MZ
SX....       rZ@MMMMMMMMM20MMMMMMMMMMM.   ,::::,.  ....7BBaa:.  ZMM88WMMMMMMMMMMMWWMMMMMMMMMMMa  aM7
rri,:..,,.             7ZMMMMMMMMMW     .,,,,,,,.  ....;Z00Xi:,   0MWari:ir722aZ2SXii     iZBZi  @ar
,ir:,,..........                      ..,,,::i,,.......,78Za7::.                           .XX   Wir
,.ri,,.,,..........   .             .....,,:::,,   .. .,;02SXi,,,,..         .  ....,,,,i:iX2:  X7 7
: :i::,..,...................... .......,,::::,,  ...,.,;20a2;i,..,...,...,,,,.,,,,..,,i:iX7X   X, ;
i  iii:,..,,.......,...... ......  ...,,:.:::,,,.  .....,r0aS;:.,,,.,,.,,,,..,,,:,,:,,::i7XXr  .7. .
i  :::,::,,,...,..,,........ ..........:,,ii::,....  ,.,,r0BB2;,,.,....,....,...,:,i::ii;7X;.  :;.,.
:. ,iiii,,,,,,,,............     ...,,,::::i:,,..   ....,iaWMM; ,,,,,..,..,,,,,,,,,:,i;rr7;.   :,.,i
,.  :iriii::,,,.......................,i::,,,,,,.  . .,,,,SB20S ..,,..,,.,,,,,:,:::ii;rrii.        .
,   .iiiii:,,,,.,,,...... ..........,:i::,.:::,,... ..,:,:XS ,2, .,..,,,.,,,.,,,:ii;i;ii,,          
: .  ::i;;i::::,.,...................::.,,,,::,... . .,,.,X7 .r2. ..,....,..,::,i:;r;r;i,.          
:    .::;;;i::,,:.,,................:i:::,,,.,,..    ..,.,2aiiiWS  .....,..,:,:i;i;r;;ii:.          
:    .:,:::ii:,::,,,................,i:..   .,.....     :rZr...SM   ......,,::::i;ir;r;,,.          
:     .:::::i::::,,,...,,.......... .i:.        ..     :X;    :70a   ...,...,,ii;rr7ri::.           
:   . .:iiii;:ii::,,.,.,............ ;i  ;ZMMMr      ,:;XBMM@i. XM:  ...,,,:::i:i;rrri:, .          
:      ,,,::iiii,:,,,,,,..........    ZM8Sa@MMMMi :irrrMMM87rr2WMM0. . ...,:::iiirri;i:,            
:    . ,,,::::iii,:,.,,,........ ...   ZMMMMMMMMM7  :,ZM08BMMMMWBBM2 ,.....,,,i;r77i;i:.            
:      .,,:::i:i;:,:,,,...,.......  .           7MMZSZB88000B00B@MMW ,,...,,::ii;;iii,.             
:       ..,,:::i::,,,.,,,................         X0MMWB0BBW@MMMMM8r.:,:..,,:,i;;;r:, .             
:     .  .,,::::i::.,.,.........................    SWMMMMMM@B2:    .:::,,..,,:i;7r,,.              
:         ..,::;ii:,...,,...,,,..,,,..,..,,.,,,,..    :7SSi:       .,::ii:,,.,,:rr;,.               
:      .   .,:::::,:,,,.,,,,,.,.,,,..,,,,,,,:,.,:::,.         ..,.:::i::ii,:,::irri.. .             
:      ..   ..iii::,.,.,.,,,,,,,,,,,:,,,,,,,...,:::,,.......,,,,,,:::::iii:iiiiir7:                 
:       .   ..,ii:,,,:.::,::,:,,.:,:,..         ..,,.             .,,::iiiiii::i;r,                 
:            ..:ii,.,,,:,:,,,,,,.,.      ,,,:,          :i::..   .     .:iii:,::ii                  
:        .... .,::::,.,,:::,,..      ,:;ri: ,:;ri,  i7XaX:i.;2S220B@WWaX;:ii:,::;:                  
:            ...,::i,,,.::,:i:,  iS0WWBWMMMMMMMMB@BaZX728WMMMMMMMMMMMMM07::::::i;.       .          
:      ..    ....:ii:,,,:ii;X2MMMMMMMMMMMBZ8Z8ZWMMMMMMMMMMMMMMMMMMMM827;:.:,,,iri       ,:          
:      ,  .    . ,ii:,.,::::,                                    .SX;i:,,,,,,,;r       :,,          
:      :. .:,    ,:i::,,,,:.........,.,. ,:iii;i:,:,i:i:.i;,,  iSar,,:::,,,:,:7;     XaXrX.         
:     .,:  .i;   .::ii,,,,:,,:,.....,:::,.,,,              .;720Xi,.:,iii:,,iXX,    WM@WB0,         
:    .: i.  .ir.  .ii;ii:,,,::::,,,,,,:i;iiiii;7rS22S2XXXX22S2Xi::ii;;rr;iii;X..   MMWBWBB2         
:    r;. ;   .i;:  .i;;;;:ii:,:::.,...::i;r7SS2aaZZ00ZSXXSX77i,,,iii777rr;rX2r:.  WMW0BBB00X:       
:    7XS;ir.   :;r   .iiiiii:::i:,:,,...........   ...,:,,.  ..:,iirX77rii72X:   MMW0B0000W0a       
:   .XaWB8SS;    ;Xi  .,,:i:i,:::::,,,,,.....,,,,,...,.......,,ii;;rX7i;ir22;   MMWBBWB00000W;      
:   r0B0BWWBZ0S   :XX   ,,.,::i:,,::,,,,.,... .......   .   .,::i:XX7r;;iSZ7  SMMWBBBBB00000BS      
:   aWB080BW0B@MS   ra7   ::,.,::,:,.... .. .     .  . .....,::i;;XS7r;;SZ;  BMMWWWBBBBB0000BB8     
:  .M@W@WWW@BB0WMM8   BM2  .,,,:,:,,.......... ..... .....,,::i;:;7XrrX2Zi .MMM@WBWBWBBBBBBBBBM     
:  XMWWW@MMWBB00WWMM;  2MW,  .,,::,,,,,,..................,,:::iiSSS7XaS. 2MMMWW@WWBBWBWBWBBB0MB    
,  0MMWW@MMMMMWWBWWMMMi 7MMM.  ,:,,,,,,,,.................,,:,:;SSZS22X.,MMMM@WW@@@@@WWWWBBWBB@M7   
  rBWM@@MMMMMMM@WBW0WMMMWXZMM8  .,..,,..,.,................,:iiiX28ZZ2;MMMM@WW@@MMMMMMM@WWWWWW@M00, 
 ;M0BMMMMMMMMMMMM@@@@WB@MMWZMMM;...::,..,..............,,.,,,::;S8Z2X2MMMWW@@MMMMMMMMMMM@WWWWW@W0WB 
 WWa8MMMMMMMMMMMMMMMM@WBBWMM@WMMWi  .,:,......,..  .    . ..,77aa27SMMMM@@MMMMMMMMMMMMMMMMM@@WMB8WW7
SMW00MMMMMMMMMMMMMMMMMMM@WWWMMM@MM@7..,:   .               ,i72Z72MMMMMMMMMMMMMMMMMMMMMMMMMMMMMB8BWS
2WBB0MMMMMMMMMMMMMMMMMMMMM@WW@MMMaWMM27ii,         .,,,:,iSSSXa@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM@0W@a
aB@WWMMMMMMMMMMMMMMMMMMMMMMM@WWMMM020@MMMMMMMM@@MMMMMMMMMWBBMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMBM@Z
ZWMMMMMMMMMMMMMMMMMMMMMMMMMMMM@@@MMM0SXX2aX72SS;;;i::   iaMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMZ
Z@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM@WMMMMZ7rr;;irr;;ii:,,r0MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8
8MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM@@@MMMMai:::;;r;;,iXBMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8
0MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM07,;ii:72BWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM0
WMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8;;7ZBW@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM0
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMZ80B@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM0
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMBaWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM0
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM@
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM0MMMMMMMMMMWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMM@W@@M@i MMMM8MMMMMMM80 Zi X 2a;r;02Z 0X2 ;MW X80@@WMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMM08B00M  M8  Xr2;  MB0ZM  MMMM2MiMMM M0MM .MMM@B@MMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMM@Z88M @ .0 Z2 XM  ZS2@  Zrr;   SZB MX8ZW  W@B0BWMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM@MMMMM0ZMMMZMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMWW00BW B2rM0WMZMSSW@WiWB8WMMZ08MMMMMMMMMMWMMMMMMMWMBMMMa2WZMMMWMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMM@MM MM MBWM MZBW@M M@BB: MBM ra0i  @S@ ,B0 08@ @SSa0: MM  M8M@WMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMM@Ba2ZM 8  B2SM M:ZZZZ MBZZ  MBW 2Zr;W 2XZ  0M  0M 22Z8W: MM  M8MM@MMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMM@@W MMa,ZZM M0XBWM,MBaBM0WM@MMM8MMM2MM@;@BXMWM7MWXW@ZXM@SMMWMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM800MZ008WWaB0MWZaMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMWBMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMB@WMMMMMMMMM@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMa 0MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM  ,MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMW00M0M@@@WX   MMMM
@MMMW27ZWaWZarZMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMB8ZaaZ2aZMX.MMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
@MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * */
public class Matrix implements Serializable {

  protected static final long serialVersionUID = 9485234592810292L;

  // The dimension of the matrix: rows by cols.
  private int rDim, cDim;
  // The matrix is stored as a 1D array of n*n values.
  // To access element [i,j], read from index n*i+j.
  // (i and j are zero-indexed).
  transient private Double[] array;

  public Matrix(Matrix src) {
    this.rDim = src.rDim;
    this.cDim = src.cDim;
    this.array = new Double[rDim * cDim];
    for (int i = 0; i < rDim; i++) {
      for (int j = 0; j < cDim; j++) {
        array[rDim * i + j] = src.get(i, j);
      }
    }
  }

  /* Matrix.java
   * James Kortman    | a1648090
   *
   * A two-dimensional array container for floating-point numbers.
   * Provides methods to read from file, generate at random, and read values
   * from coordinates within the matrix.
   */
  public Matrix(int rDim, int cDim, List<List<Double>> weights) throws IllegalArgumentException {
    if (rDim < 0 || cDim < 0) {
      throw new IllegalArgumentException("Dimensions less than 0");
    }
    this.rDim = rDim;
    this.cDim = cDim;
    this.array = new Double[rDim * cDim];
    furnishArray(array, weights, rDim, cDim);
  }

  public Matrix(List<List<Double>> weights) {
    this.rDim = weights.size();
    this.cDim = weights.get(0).size();
    this.array = new Double[rDim * cDim];
    furnishArray(array, weights, rDim, cDim);
  }

  private void furnishArray(Double[] array, List<List<Double>> weights, int rDim, int cDim) {
    for (int i = 0; i < rDim; i++) {
      for (int j = 0; j < cDim; j++) {
        if (weights != null) {
          array[rDim * i + j] = weights.get(i).get(j);
        } else {
          array[rDim * i + j] = 0.0;
        }
      }
    }
  }

  public int size() {
    return this.rDim;
  }

  // Getter to read a value at a coordinate in the matrix.
  // Provided a valid set of coordinates i,j where 0 <= i,j < n,
  // returns the value at row i, column j of the contained matrix.
  // If i or j are outside of the valid range, throws
  // IllegalArgumentException.
  public double get(int i, int j) throws IndexOutOfBoundsException {
    if (i < 0 || i >= rDim || j < 0 || j >= cDim) {
      throw new IndexOutOfBoundsException("coordinates out of bounds");
    }
    return array[rDim * i + j];
  }

  // Setter to set values to coordinates in the matrix.
  // Provided a valid set of coordinates i,j where 0 <= i,j < n,
  // and a double val, sets element [i,j] of the matrix to val.
  // If i or j are outside of the valid range, throws IllegalArgumentException.
  public void set(int i, int j, double val) throws IndexOutOfBoundsException {
    if (i < 0 || i >= rDim || j < 0 || j >= cDim) {
      throw new IndexOutOfBoundsException("coordinates out of bounds");
    }
    array[rDim * i + j] = val;
  }

  // Prints the entire matrix to stdout for debugging purposes.
  public void print() {
    DecimalFormat df = new DecimalFormat("#0.00");
    for (int i = 0; i < rDim; i += 1) {
      System.out.print("  [ ");
      for (int j = 0; j < cDim; j += 1) {
        System.out.print("" + df.format(this.get(i, j)));
        if (j < cDim - 1) {
          System.out.print(", ");
        }
      }
      System.out.print(" ]\n");
    }
  }
}
