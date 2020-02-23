package mockito;

public class Add {




    /*
     * Porque utilizar Mocks
     * Los test unitarios deben probar la funcionalidad de cada metodo de la clase de forma aislada
     * sin tener dependecia de otra clase o metodo
     *
     * En este caso queremos probar add pero depende de validatorNumber por eso debemos crear un mock de validNumber
     *
     * Imaginemos que la clase validNumber esta mal pero si la clase add esta bien dioseñada el test debe pasar
     * idependientemente que los objetos-clases mockeadas hagan bien su trabajo
     * Se debe asegurar que la clase add funcione y se debe mockear los demas objetos y clases de las que dependa add
     *
     * */

    private ValidNumber validNumber;
    private Print print;

    //Constructor1
    public Add(ValidNumber validNumber){
        this.validNumber = validNumber;
    }

    //Constructor2
    public Add(ValidNumber validNumber, Print print){
        this.validNumber = validNumber;
        this.print = print;
    }

    //Metodo para sumar
    public int add(Object a, Object b){
        if(validNumber.check(a) && validNumber.check(b)){
            return (Integer)a + (Integer)b;
        }
        return 0;
    }

    public int addInt(Object a){
        return validNumber.doubleToInt(a) + validNumber.doubleToInt(a);
    }

    public void addPrint(Object a, Object b){
        if(validNumber.check(a) && validNumber.check(b)){
            int result = (Integer)a + (Integer)b;
            print.showMessage(result);
        }else {
            print.showError();
        }
    }

}
