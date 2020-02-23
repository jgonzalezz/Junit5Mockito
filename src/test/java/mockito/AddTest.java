package mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

//https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
//https://github.com/mockito/mockito/wiki
//https://github.com/mockito/mockito/wiki/FAQ

class AddTest {

    @InjectMocks
    private Add add;
    @Mock
    private ValidNumber validNumber;
    @Mock
    private Print print;

    @Captor
    private ArgumentCaptor<Integer> captorInt;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    //thenReturn
    @Test
    public void addTest(){

        //Mockeamos el valor de retorno
        Mockito.when(validNumber.check(3)).thenReturn(true);// (Stubbing) Setear valor de retorno que queremos  "true"

        //Luego testeamos
        boolean checkNumber = validNumber.check(3);
        assertEquals(true, checkNumber);

        when(validNumber.check("a")).thenReturn(false);
        checkNumber = validNumber.check("a");
        assertEquals(false, checkNumber);
    }

    //thenThrow      (Exception)
    @Test
    public void addMockExceptionTest(){
        when(validNumber.checkZero(0)).thenThrow(new ArithmeticException("No podemos aceptar cero"));
        Exception exception = null;
        try{
            validNumber.checkZero(0);
        }catch (ArithmeticException e){
            exception = e;
        }
        assertNotNull(exception);
    }


    //Llamar al metodo de dependencia real --  Cuando estemos seguros de  que se ejecute correctamente
    @Test
    public void addRealMethodTest(){
        when(validNumber.check(3)).thenCallRealMethod();
        assertEquals(true, validNumber.check(3));

        when(validNumber.check("3")).thenCallRealMethod();
        assertEquals(false, validNumber.check("3"));
    }

    //Answer para devolver respuestas mas inteligentes o complejas
    @Test
    public void addDoubleToIntThenAnswerTest(){
        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                //podemos agregar logica para retornar el objeto...
                return 7;
            }
        };

        when(validNumber.doubleToInt(7.7)).thenAnswer(answer);
        assertEquals(14, add.addInt(7.7));
       // assertEquals(14, add.addInt(8.7)); Sale error y el mock nos devuelve el valor por defecto cero

    }


    //Patron de pruebas para mock
    //Arrange - organizar toodo lo que queremos testear setep
    //Act - activacion de los metodos
    //Assert -

    // son  igual a:

    //Given - Arrange setup
    //When - act Activacion
    //Then - Assert que es lo que va ocurrir

    @Test
    public void patterTest(){
        //Arrange
        when(validNumber.check(4)).thenReturn(true);
        when(validNumber.check(5)).thenReturn(true);
        //Act
        int result = add.add(4,5);
        //Assert
        assertEquals(9, result);
    }

    //https://www.baeldung.com/bdd-mockito
    @Test
    public void patterBDDTest(){
        //Given  - dado tal cosa - retornara esto
        given(validNumber.check(4)).willReturn(true); // will el futuro
        given(validNumber.check(5)).willReturn(true);
        //When
        int result = add.add(4,5);
        //Then
        assertEquals(9, result);
    }

    //ArgumentMatcher cualquier calor -  en este caso dado cualquier entero anyInt()
    @Test
    public void argumentMatcherTest(){
        //Given
        given(validNumber.check(anyInt())).willReturn(true);
        //When
        int result = add.add(4,5);
        //Then
        assertEquals(9, result);
    }



    //TEstear metodos void
    @Test
    public void addPrintTest(){
        //Given - dado talcosa retornara esto
        given(validNumber.check(4)).willReturn(true);
        given(validNumber.check(5)).willReturn(true);
        //When
        add.addPrint(4,5);
        //Then
        verify(validNumber).check(4); //verifica que el
        //verify(validNumber, times(2)).check(4); //verificar que el metodo se ha ejecutado dos veces
        verify(validNumber, never()).check(99);//verificar que nunca ha sido invocado
        verify(validNumber, atLeast(1)).check(4); //numero minimo de veces qu e se ejecuto
        verify(validNumber, atMost(3)).check(4);// como mucho

        verify(print).showMessage(9); //verifique print
        verify(print, never()).showError();//verifique showerror never porque con los datos mockeados no pasa por el showError
    }

    //captor - se usa para capturar el valor de las aserciones
    @Test
    public void captorTest(){
        //Given
        given(validNumber.check(4)).willReturn(true);
        given(validNumber.check(5)).willReturn(true);
        //When
        add.addPrint(4,5);
        //Then
        verify(print).showMessage(captorInt.capture());//capturamos el argumento en este caso 9 que se calculo en la suma
        assertEquals(captorInt.getValue().intValue(), 9);
    }

    //Spy a veces tenemos que llamar a metodos reales
    //Igual que un mock pero con metodos reales y mockeados
    @Spy
    List<String> spyList = new ArrayList<>();
    @Mock
    List<String> mockList = new ArrayList<>();

    @Test
    public void spyTest(){
        spyList.add("1");
        spyList.add("2");
        verify(spyList).add("1");//verifica que en el spylist se ha ejecutado el metodo add con el 1
        verify(spyList).add("2");//verifica que en el spylist se ha ejecutado el metodo add con el 2
        assertEquals(2, spyList.size());//se comporta como un metodo real
    }

    @Test
    public void mockTest(){
        mockList.add("1");
        mockList.add("2");
        verify(mockList).add("1");
        verify(mockList).add("2");

        //se debe mockear con el given
        given(mockList.size()).willReturn(2);

        assertEquals(2, mockList.size());

    }

}