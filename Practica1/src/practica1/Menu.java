package practica1;
//@author wdani
import java.io.*;
import java.util.Scanner;
import java.lang.Math;
import java.math.BigDecimal;

public class Menu {
    public String reporte;
    public int filasde;
    public int columnasde;

    public Menu(){
        Scanner leer = new Scanner(System.in);
        byte opcion = 0;
        while (opcion!=4) {
            escribir("*************************** Menú ***************************\n"
                    + "*                                                          *\n"
                    + "* 1) Cifrar mensaje                                        *\n"
                    + "* 2) Descifrar mensaje                                     *\n"
                    + "* 3) Resolver matriz utilizando el método de Gauss-Jordan  *\n"
                    + "* 4) Salir                                                 *\n"
                    + "*                                                          *\n"
                    + "************************************************************");


            opcion = leer.nextByte();
            switch (opcion) {
                case 1:
                    Cifrar();
                    break;
                case 2:
                    Descifrar();
                    break;
                case 3:
                    Gauss();
                    break;
                case 4:
                    escribir("Programa finalizado");
                    break;
                default:
                    escribir("Opción no valida");
            }
        }
            
    }
    
    private void escribir(String mensaje){
        System.out.println(mensaje);
    }
    private void escribirsinsalto(String mensaje){ System.out.print(mensaje);}
    
    private void Cifrar(){

        Scanner leer = new Scanner(System.in);

        try{
            escribir("************************* Cifrado **************************\n\n"
                    + "Ingrese el mensaje que desea cifrar: (Mínimo 3 carácteres)");
            String texto = leer.nextLine();
            reporte="<h1>El texto ingresado es: </h1>\n" +
                    "<p>"+texto+"</p>";
            int matrizascii[][] = convertirascii(texto);
            escribir("Ingrese la ruta del archivo en donde se ecuentra la matriz:");
            String ruta = leer.nextLine();
            int matrizentrada[][] = leermatriz(ruta,texto);
            escribir("");
            escribir("El mensaje \""+ texto+ "\" cifrado es:");
            escribir("");
            cifrado(matrizascii,matrizentrada,filas(texto));
            escribir("");
            generarreporte("Cifrado","ReporteCifrado");
        }catch(Exception e){
            escribir("Ocurrio un error: "+ e.getMessage());
        }
    }
    private int[][] convertirascii(String texto){
        try{
            int largo = texto.length();
            int numfilas = filas(texto);
            int numcolumnas = 0;
            if(numfilas>=3){
                numcolumnas = largo/numfilas;
                int i = 0;
                int j = 0;
                int matrizi[][] = new int[numfilas][numcolumnas];
                char matrizs[][]= new char[numfilas][numcolumnas];

                for (char caracter : texto.toCharArray()) {
                    matrizs[i][j] = caracter;
                    matrizi[i][j] = caracter;
                    j++;
                    if(j%numcolumnas==0){
                        j=0;
                        i++;
                    }
                }
                generarmatrizchar("Matriz de texto",matrizs);
                generarmatrizint("Matriz del texto convertido a ASCII",matrizi);
                return matrizi;

            }else {
                escribir("El mensaje debe tener más de 3 carácteres");
                return null;
            }
        }catch (Exception e){
            escribir("Ocurrio un error " + e.getMessage());
            return null;
        }
    }
    private int filas(String texto){
        int largo = texto.length();
        int numfilas = 0;
        if(largo>=3) {
            if (largo % 3 == 0) {
                numfilas = 3;
            } else if (largo % 4 == 0) {
                numfilas = 4;
            } else if (largo % 5 == 0) {
                numfilas = 5;
            } else if (largo % 7 == 0) {
                numfilas = 7;
            } else if (largo % 11 == 0) {
                numfilas = 11;
            } else if (largo % 13 == 0) {
                numfilas = 13;
            } else if (largo % 17 == 0) {
                numfilas = 17;
            } else {
                escribir("Solo se tiene contemplado hasta multiplo de 17");
            }
            return numfilas;
        }else{
            return 0;
        }
    }

    private int[][] leermatriz(String ruta, String texto){
        try{
            FileReader archivo = new FileReader("./"+ruta);
            Scanner leerarchivo = new Scanner(archivo);
            int longitud = filas(texto);
            int matriz[][] = new int[longitud][longitud];
            int j=0;
            while(leerarchivo.hasNextLine()){
                String linea[] = leerarchivo.nextLine().split(",");
                for(int i = 0; i<linea.length;i++){
                    matriz[j][i]=Integer.parseInt(linea[i]);
                }
                j++;    
            }
            generarmatrizint("Matriz clave",matriz);
            return matriz;

        }catch(FileNotFoundException e){
            escribir("No se encuentra el archivo");
            e.printStackTrace();
            return null;
        }
    }

    private void cifrado(int matrizascii[][], int matrizentrada[][], int filas){
        int matrizcifrada[][] = new int[matrizascii.length][matrizascii[0].length];
        for(int i=0;i<filas;i++){
            for(int j=0;j<matrizascii[i].length;j++) {
                for (int k = 0; k < filas; k++) {
                    matrizcifrada[i][j] += (matrizentrada[i][k] * matrizascii[k][j]);
                }
                escribirsinsalto(String.valueOf(matrizcifrada[i][j])+" \t");
            }
            escribir("");
        }
        escribir("");
        generarmatrizint("Matriz del resultante",matrizcifrada);
    }


    private void Descifrar(){
        Scanner leer = new Scanner(System.in);

        try{
            BigDecimal decimal;
            escribir("************************* Descifrado **************************\n\n"
                    + "Ingrese la ruta de la matriz clave (N*N)");
            String ruta = leer.nextLine();
            calculofilas1(ruta);
            int matrizclave[][] = leermatrizclave(ruta);
            escribir("Ingrese la ruta de matriz del texto cifrado (N*M)");
            ruta = leer.nextLine();
            calculocolumnas1(ruta);
            int fraseint[][] = leermatrizascii(ruta);
            double matrizclaveinversa[][] = inversa(matrizclave);
            double matrizdescifrada[][] = new double[matrizclaveinversa.length][fraseint[0].length];

            for(int i=0;i<matrizclaveinversa.length;i++){
                for(int j=0;j<fraseint[i].length;j++) {
                    for (int k = 0; k < matrizclaveinversa.length; k++) {
                        matrizdescifrada[i][j] += (matrizclaveinversa[i][k] * fraseint[k][j]);
                    }
                }
            }
            generarmatrizdb("Se multiplica la inversa de la clave por la matriz de texto cifrado", matrizdescifrada);
            
            for (int i=0;i< matrizdescifrada.length;i++){
                for(int j=0;j< matrizdescifrada[0].length;j++){
                    decimal = new BigDecimal(matrizdescifrada[i][j]);
                    matrizdescifrada[i][j] = decimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                }
            }
            generarmatrizdb("Se aproxima al entero más cercano",matrizdescifrada);
            char matrizdescifradachar[][]=new char[matrizdescifrada.length][matrizdescifrada[0].length];
            String mensajedescifrado="";
            for (int i=0;i< matrizdescifrada.length;i++){
                for(int j=0;j< matrizdescifrada[0].length;j++){
                    matrizdescifradachar[i][j] = (char)((int)matrizdescifrada[i][j]);
                    mensajedescifrado += matrizdescifradachar[i][j];
                }
            }
            generarmatrizchar("Se convierte a char la matriz descifrada",matrizdescifradachar);

            escribir("El mesaje es: " + mensajedescifrado);
            reporte += "<h2>El mensaje descifrado es:" + mensajedescifrado+"<h2>";


            generarreporte("Descifrar","ReporteDescifrar");
        }catch(Exception e){
            escribir("Ocurrio un error: "+ e.getMessage());
        }
    }

    private void calculocolumnas1(String ruta) {
        try{
            Scanner leerint = new Scanner(new FileReader(ruta));
            String fila[]= leerint.nextLine().split(",");
            columnasde= fila.length;
        }catch(FileNotFoundException e){
            escribir("No se encuentra el archivo");
            e.printStackTrace();
            columnasde=0;
        }
    }

    private void calculofilas1(String ruta) {
        try{
            Scanner leerint = new Scanner(new FileReader(ruta));
            String fila[]= leerint.nextLine().split(",");
            filasde= fila.length;
        }catch(FileNotFoundException e){
            escribir("No se encuentra el archivo");
            e.printStackTrace();
            filasde=0;
        }
    }

    private int[][] leermatrizclave(String ruta) {
        try{
            Scanner leerint = new Scanner(new FileReader(ruta));
            int matriz[][] = new int[filasde][filasde];
            int i = 0;
            while(leerint.hasNextLine()){
                String linea[] = leerint.nextLine().split(",");
                for (int j=0;j< linea.length;j++){
                    matriz[i][j]= Integer.parseInt(linea[j]);
                    escribirsinsalto(matriz[i][j]+"\t");
                }
                escribir("");
                i++;
            }
            generarmatrizint("Matriz clave ingresada",matriz);
            return matriz;
        }catch(FileNotFoundException e){
            escribir("No se encuentra el archivo");
            e.printStackTrace();
            return null;
        }
    }

    private int[][] leermatrizascii(String ruta) {
        try{
            Scanner leerint = new Scanner(new FileReader(ruta));
            int matriz[][] = new int[filasde][columnasde];
            int i = 0;
            while(leerint.hasNextLine()){
                String linea[] = leerint.nextLine().split(",");
                for (int j=0;j< linea.length;j++){
                    matriz[i][j]= Integer.parseInt(linea[j]);
                    escribirsinsalto(matriz[i][j]+" \t");
                }
                escribir("");
                i++;
            }
            generarmatrizint("Matriz con texto cifrado ingresada",matriz);
            return matriz;
        }catch(FileNotFoundException e){
            escribir("No se encuentra el archivo");
            e.printStackTrace();
            return null;
        }
    }

    private double[][] inversa(int[][] matrizclave) {

        double matrizinversa[][]=new double[matrizclave.length][matrizclave[0].length];
        for(int i=0;i< matrizinversa.length;i++){
            for (int j=0;j<matrizinversa.length;j++){
                matrizinversa[i][j]=matrizclave[i][j];
            }
        }
        int filasin = matrizinversa.length;
        double sustituir[][] = new double[filasin][filasin];
        double b[][] = new double[filasin][filasin];
        int indice[] = new int[filasin];
        for (int i=0; i<filasin; ++i) {
            b[i][i] = 1;
        }
        
        generarmatrizdb("Matriz triangula para operar", b);

        gausssuperior(matrizinversa,indice);

        for (int i=0; i<filasin-1; ++i){
            for (int j=i+1; j<filasin; ++j){
                for (int k=0; k<filasin; ++k){
                    b[indice[j]][k]-= matrizinversa[indice[j]][i]*b[indice[i]][k];
                }
            }
        }
        
        generarmatrizdb("Al valor de la matriz b se le restan la matriz inversa por la matriz b",b);
        
        for (int i=0; i<filasin; ++i){
            sustituir[filasin-1][i] = b[indice[filasin-1]][i]/matrizinversa[indice[filasin-1]][filasin-1];
            for (int j=filasin-2; j>=0; --j){
                sustituir[j][i] = b[indice[j]][i];
                for (int k=j+1; k<filasin; ++k){
                    sustituir[j][i] -= matrizinversa[indice[j]][k]*sustituir[k][i];
                }
                sustituir[j][i] /= matrizinversa[indice[j]][j];
            }
        }
        generarmatrizdb("Se sustituye de atras hacia adelante en la matriz invera", sustituir);
        return sustituir;
    }

    private void gausssuperior(double[][] matrizinversa, int[] indice) {
        int indice1 = indice.length;
        double c[] = new double[indice1];

        for (int i=0; i<indice1; ++i){
            indice[i] = i;
        }
        for (int i=0; i<indice1; ++i){
            double c1 = 0;
            for (int j = 0; j < indice1; ++j){
                double c0 = Math.abs(matrizinversa[i][j]);
                if (c0 > c1){
                    c1 = c0;
                }
            }
            c[i] = c1;
        }
        int k = 0;
        for (int j=0; j<indice1-1; ++j){
            double pi1 = 0;
            for (int i = j; i < indice1; ++i){

                double pi0 = Math.abs(matrizinversa[indice[i]][j]);
                pi0 /= c[indice[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            int itmp = indice[j];
            indice[j] = indice[k];
            indice[k] = itmp;
            for (int i = j + 1; i < indice1; ++i) {
                double pj = matrizinversa[indice[i]][j] / matrizinversa[indice[j]][j];

                matrizinversa[indice[i]][j] = pj;

                for (int l = j + 1; l < indice1; ++l) {
                    matrizinversa[indice[i]][l] -= pj * matrizinversa[indice[j]][l];
                }
            }
        }
        generarmatrizdb("Se utiliza el método de Gauss para hacer eliminación", matrizinversa);
    }

    private void Gauss(){
        Scanner leer = new Scanner(System.in);
        try{
            escribir("******************* Matriz Gauss-Jordan ********************\n");
            escribir("Ingrese la ruta del archivo en donde se ecuentra la matriz:");
            String ruta = leer.nextLine();
            escribir("La matriz ingresada es:");
            int matrizentrada[][] = leermatrizGauss(ruta);
            if(matrizentrada.length==3 && matrizentrada[0].length==4){
                resolverGauss(matrizentrada);
                generarreporte("Gauss Jordan","ReporteGauss");
            }else{
                escribir("La matriz debe ser de 3*4 Para poder resolverla");
            }
        }catch(Exception e){
            escribir("Ocurrio un error: "+ e.getMessage());
        }
    }

    private void resolverGauss(int[][] matrizentrada) {
        double matriz[][]=new double[matrizentrada.length][matrizentrada[0].length];
        
        if(matrizentrada.length == 3 && matrizentrada[0].length == 4){
            if(matrizentrada[0][0]!=1){
            for(int i=0;i< matrizentrada[0].length;i++){
                matriz[0][i]=(matrizentrada[0][i]/matrizentrada[0][0]);
            }
            generarmatrizdb("Se vuelve 1 la posición [1][1], si es necesario",matriz);
        }else{
            for(int i=0;i< matrizentrada[0].length;i++){
                matriz[0][i]=matrizentrada[0][i];
            }
        }
        for(int i=1;i< matrizentrada.length;i++){
            for(int j=0;j<matrizentrada[0].length;j++){
                matriz[i][j]=matrizentrada[i][j]-(matriz[0][j]*matrizentrada[i][0]);
            }
        }
        generarmatrizdb("Se vuelve 0 la fila 2 y 3 en la columna 1",matriz);
        double indice2= matriz[1][1];
        for(int i=1;i< matrizentrada[0].length;i++){
            matriz[1][i]=matriz[1][i]/indice2;
        }
        generarmatrizdb("Se vuleve 1 la posición [2][2]",matriz);
        double valor1 = matriz[0][1];
        double valor3 = matriz[2][1];

        for(int i=0;i< matrizentrada.length;i++){
            for(int j=1;j<matrizentrada[0].length;j++){
                if (i==1){
                    continue;
                }else if(i==0){
                    matriz[i][j]=matriz[i][j]-(matriz[1][j]*valor1);
                }else{
                    matriz[i][j]=matriz[i][j]-(matriz[1][j]*valor3);
                }
            }
        }
        generarmatrizdb("Se vuelve 0 el resto de filas en la columna 2", matriz);
        double indice3 = matriz[2][2];
        for(int j=2;j<matrizentrada[0].length;j++){
            matriz[2][j]=matriz[2][j]/indice3;
        }
        generarmatrizdb("Se vuleve 1 la posición [3][3]",matriz);
        double valor1_2 = matriz[0][2];
        double valor2_2 = matriz[1][2];

        for(int i=0;i< matrizentrada.length;i++){
            for(int j=2;j<matrizentrada[0].length;j++){
                if (i==2){
                    continue;
                }else if(i==0){
                    matriz[i][j]=matriz[i][j]-(matriz[2][j]*valor1_2);
                }else{
                    matriz[i][j]=matriz[i][j]-(matriz[2][j]*valor2_2);
                }
            }
        }
        generarmatrizdb("Se vuelve 0 el resto de filas en la columna 3", matriz);
        reporte+="<h3>El resultado es: </h3>";
        reporte+="<h3>X = "+matriz[0][3]+ "</h3>";
        reporte+="<h3>Y = "+matriz[1][3]+ "</h3>";
        reporte+="<h3>Z = "+matriz[2][3]+ "</h3>";
        escribir("\nEl resultado es:");
        escribir("X = " + matriz[0][3]);
        escribir("Y = "+ matriz[1][3]);
        escribir("Z = "+matriz[2][3]);

        }else{
            escribir("La matriz debe ser de 3*4 para poder resolverla");
        }
    }

    private int[][] leermatrizGauss(String ruta) {
        try{
            Scanner leergauss = new Scanner(new FileReader(ruta));
            int[][] matriz = new int[3][4];
            int i = 0;
            while(leergauss.hasNextLine()){
                String linea[] = leergauss.nextLine().split(",");
                for (int j=0;j<4;j++){
                    matriz[i][j]= Integer.parseInt(linea[j]);
                    escribirsinsalto(matriz[i][j]+"\t");
                }
                escribir("");
                i++;
            }
            generarmatrizint("Matriz Ingresada",matriz);
            return matriz;
        }catch(FileNotFoundException e){
            escribir("No se encuentra el archivo");
            e.printStackTrace();
            return null;
        }
    }


       private void generarmatrizchar(String nombre, char matriz[][]){
        reporte +="<h1>"+nombre+"</h1>\n" +
                "<center><table border=\"2\">\n";
        for(int i=0;i< matriz.length;i++){
            reporte+="<tr>\n";
            for(int j=0;j<matriz[0].length;j++){
                reporte+="<td>"+matriz[i][j]+"</td>";
            }
            reporte+="</tr>";
        }
        reporte+="</table></center>";
    }

    private void generarmatrizint(String nombre, int matriz[][]){
        reporte +="<h1>"+nombre+"</h1>\n" +
                "<center><table border=\"2\">\n";
        for(int i=0;i< matriz.length;i++){
            reporte+="<tr>\n";
            for(int j=0;j<matriz[0].length;j++){
                reporte+="<td>"+matriz[i][j]+"</td>";
            }
            reporte+="</tr>";
        }
        reporte+="</table></center>";
    }
    private void generarmatrizdb(String nombre, double matriz[][]){
        reporte +="<h1>"+nombre+"</h1>\n" +
                "<center><table border=\"2\">\n";
        for(int i=0;i< matriz.length;i++){
            reporte+="<tr>\n";
            for(int j=0;j<matriz[0].length;j++){
                reporte+="<td>"+matriz[i][j]+"</td>";
            }
            reporte+="</tr>";
        }
        reporte+="</table></center>";
    }

    private void generarreporte(String titulo, String ruta){
        String html = "<html>\n" +
                "<head>\n" +
                "<link href=\"estilo.css\" rel=\"Stylesheet\" type=\"text/css\">\n" +
                "<title>"+titulo+"</title>\n" +
                "</head>\n" +
                "<body>\n"+ reporte+"\n</body>\n" +
                "</html>";

        try {
            BufferedWriter escribirreporte = new BufferedWriter(new FileWriter("Reportes/"+ruta+".html",false));
            escribirreporte.write(html);
            escribirreporte.close();
            escribir("Se generó el reporte \""+ruta+".html\" en la carpeta Reportes");
            reporte="";
        }catch(IOException e){
            escribir("Ocurrio un error al escribir el reporte");
        }
    }
}
