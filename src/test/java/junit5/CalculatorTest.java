package junit5;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;


/**
 * Created by albertopalomarrobledo on 2/10/19.
 */
class CalculatorTest {

    private Calculator calculator;
    private Calculator calculatorNull;
    private static Calculator calculatorStatic;

    /*
     //METODO////////////////////////////////////////ESPECIFICACIOM//////////////////////////////////////////////////////////////////////////////
    int sumar(                  |Este método devuelve un int resultado de la suma de numero 1 y numero2
        int numero1,            |
        int numero2)            |
    ------------------------------------------------------------------------------------------------------------------------
    int restar(                 |Este método devuelve un int resultado de la resta de numero 1 y numero2
        int numero1,            |
        int numero2)            |


    Método a Probar                 |      Entrada      |       Salida Esperada
    sumar(int a, int b)             |a = 10, b=20       |30
    sumar(int a, int b)             |a = 7, b=4         |11
    restar(int a, int b)            |a = 7, b=4         |3
    restar(int a, int b)            |a = 10, b=20       |-10
     */


    /*
    * Etiquetas
    *
    * @Test : se indica a junit que se trata de un metodo test
    * @
    * @
    * @
    * @
    * @
    * */

    @BeforeAll //se ejecuta SOLO una vez al principio      //Ej.  inicializar bases de datos debe ser STATIC
    public static void beforeAllTests(){
        calculatorStatic = new Calculator();
        System.out.println("@BeforeAll -> beforeAllTests()");
    }

    @BeforeEach    //Se ejecuta siempre antes de cada uno de los metodos @Test  // iniciar variables e instaciar objetos
    public void setUp(){
        calculator = new Calculator();
        System.out.println("@BeforeEach -> setUp()");
    }

    @AfterEach //Se ejecuta siempre despues de cada uno de los metodos @Test // metodo de limpieza y liberar recursos
    public void tearDown(){
        calculator = null;
        System.out.println("@AfterEach -> tearDown()");
    }

    @AfterAll //se ejecuta solo una vez al final //DEBE SER STATIC liber recursos inicalizados en el before all
    public static void afterAllTests(){
        calculatorStatic = null;
        System.out.println("@AfterAll -> afterAllTests()");
    }



    @Test
    public void calculatorNotNullTest(){
        assertNotNull(calculatorStatic, "Calculator debe ser not null"); //El mensaje solo sale cuando falla el test
        assertNotNull(calculator, "Calculator debe ser not null");
        System.out.println("@Test -> calculatorNotNullTest()");
    }

    @Test
    public void calculatorNullTest(){
        assertNull(calculatorNull);
        System.out.println("@Test -> calculatorNullTest()");
    }

    /*
    Método a Probar                 |      Entrada      |       Salida Esperada
    add(int a, int b)               |a = 10, b=20       |30
     */

    @Test
    public void addAssertTest(){
        //1.- SetUp
        int resultadoEsperado = 30;
        int resultadoActual;
        //2.- Action
        resultadoActual = calculator.add(10,20);
        //3.- Assert
        assertEquals(resultadoEsperado, resultadoActual);
        System.out.println("@Test -> addAssertTest()");
    }

    @Test// TEst optimizado
    public void addTest(){
        assertEquals(30, calculator.add(10,20));
    }


    @Test
    public void assertTypesTest(){  //Diferentes assert


        assertTrue(1 == 1);
        //assertTrue(1 == 2);

        assertNull(calculatorNull);
        assertNotNull(calculator);

        Calculator calculator1 = new Calculator();
        Calculator calculator2 = new Calculator();
        Calculator calculator3 = calculator1;   //Apuntan a la misma direccion de memoria son el mismo

        //Comprobar si son el mismo objeto
        assertSame(calculator1, calculator3); //True
       // assertSame(calculator1, calculator2); //Fase

        assertNotSame(calculator1, calculator2);//True
        //assertNotSame(calculator1, calculator3);//false


        assertEquals("alberto", "alberto");
       // assertEquals("alberto", "albert", "Ha fallado nuestro metodo String");

        assertEquals(1, 1.4, 0.5); //Delta 0.5 rango aceptable entre el intervalo 1 y 1.4  true
       // assertEquals(1, 1.6, 0.5); //false
    }

    //Ejemplos como nombrar los TEST estandar
    @Test
    public void add_ValidInput_ValidExpected_Test(){
        assertEquals(11, calculator.add(7,4));
    }
    @Test
    public void subtract_ValidInput_ValidExpected_Test(){
        assertEquals(11, calculator.subtract(15,4));
    }
    @Test
    public void subtract_ValidInput_ValidNegativeResultExpected_Test(){
        assertEquals(-10, calculator.subtract(10,20));
    }


    @Test
    public void divide_ValidInput_ValidResultExpected_Test(){
        assertEquals(2, calculator.divide(10,5));
    }
    @Test
    public void divide_InValidInput_Test(){
        fail("Fallo detectado manualmente - No se puede dividir por cero");
        calculator.divide(10,0);
    }

    //Assert exception assertThrows
    @Test
    public void divide_InValidInput_ExpectedException_Test(){
        assertThrows(ArithmeticException.class, () -> calculator.divideByZero(2,0), "No se puede dividir por cero");
    }


    //Se deshabilita el test para que no se corra y se agrega un comentario con el motivo
    @Disabled("Disabled until bug 23 be resolved")
    @Test
    public void divide_InvalidInput_Test(){
        assertEquals(2, calculator.divide(5,0));
    }

    @Test
    @DisplayName("Metodo Dividir -> Funciona") //Texto para identificar el test
    public void divide_ValidInput_ValidResultExpected_Name_Test(){
        assertEquals(2, calculator.divide(10,5));
    }

    //SI un accert falla se ejecutan kis demas de esta manera// la recomendacion es tener un solo accert por metodo
    @Test
    public void add_Assert_All_Test(){
        assertAll(
                ()-> assertEquals(31, calculator.add(11,20)),
                ()-> assertEquals(30, calculator.add(10,20)),
                ()-> assertEquals(2, calculator.add(1,1))
        );
    }

    //Varios test con diferentes respuestas o parametros de un mismo metodo para organizarlos en una subclase
    @Nested
    class AddTest{
        @Test
        public void add_Positive_Test(){
            assertEquals(30, calculator.add(16,15));
        }

        @Test
        public void add_Negative_Test(){
            assertEquals(-30, calculator.add(-15,-15));
        }

        @Test
        public void add_Zero_Test(){
            assertEquals(0, calculator.add(0,0));
        }
    }



    //Prueba parametrizada
    //los datos se pueden obtener de una fuente de dato o quemados
    //Ejecutar la misma una y otra vez con diferentes valores
    //Se debe agregar la dependencia junit-jupiter-params

    /*
    Ejemplo en nuestra división queremos hacer 5 pruebas
    Positivo / Positivo = Positivo
    Positivo / Negativo = Negativo
    Negativo / Positivo = Negativo
    Negativo / Negativo = Positivo
    Positivo / 0 = Excepción
    Método a Probar                 |      Entrada      |       Salida Experarada
    dividir(int a, int b)           |a = 6, b=2         |3
    dividir(int a, int b)           |a = 6, b=-2        |-3
    dividir(int a, int b)           |a = -6, b=2        |-3
    dividir(int a, int b)           |a = -6, b=-2       |3
    dividir(int a, int b)           |a = -6, b=0        |Excepción
     */
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @MethodSource("addProviderData")
    public void addParameterizedTest(int a, int b, int sum){
        assertEquals(sum, calculator.add(a, b));
    }

    private static Stream<Arguments> addProviderData(){
        return Stream.of(
                Arguments.of(6,2,8), //(a,b,sum)
                Arguments.of(-6,-2,-10),
                Arguments.of(6,-2,4),
                Arguments.of(-6,2,-4),
                Arguments.of(6,0,6)
        );
    }


    //Detener el test cuando tarda mucho la ejecucion del metodo
    @Test
    public void timeOut_Test(){
        assertTimeout(Duration.ofMillis(2000), () ->{
            calculator.longTaskOperation();
        });
    }

}