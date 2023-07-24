package org.example.models;

import org.example.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    @BeforeEach
    void initMetodoTest(){
        System.out.println("Iniciando el método");
    }
    @AfterEach
    void tearDown(){
        System.out.println("Finalizando el método");
    }
    @BeforeAll
    static void beforeAll() {
        System.out.println("Iniciando el test");
    }
    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }
    @Test
    @DisplayName("Probando nombre de la cuenta corriente")
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        //cuenta.setPersona("Andres");
        String esperado = "Andres";
        String actual = cuenta.getPersona();
        assertNotNull(actual, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, actual, () -> "El nombre de la cuenta no es el que se esperaba");
        assertEquals("Andres", actual, () -> "El nombre de cuenta esperada debe ser igual a la real");
    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta(){
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        assertEquals(cuenta, cuenta2);
        //assertNotEquals(cuenta, cuenta2);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Valentino", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }
    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Valentino", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Valentino", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
           cuenta.debito(new BigDecimal("1100"));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Valentino", new BigDecimal("1500"));
        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertEquals("1000", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());
    }

    @Test
    @Disabled
    void testRelacionBancoCuentas() {
        fail(); //Fuerzo un error para utilizar el disabled
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Valentino", new BigDecimal("1500"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
        assertAll(
            () -> {
                assertEquals("1000", cuenta2.getSaldo().toPlainString());
                },
            () -> {
                assertEquals("3000", cuenta1.getSaldo().toPlainString());
                },
            () -> {
                assertEquals(2, banco.getCuentas().size());
            },
            () -> {
                assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
            },
            () -> {
                assertEquals("Valentino", banco.getCuentas().stream().
                    filter(c -> c.getPersona().equals("Valentino")).findFirst().get().getPersona());
            },
            () -> {
                assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Valentino")));
            });
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows(){
        }
        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC })
            void testSoloLinux(){
        }
        @Test
        @DisabledOnOs(OS.WINDOWS)
            void testNoWindows(){
        }

        @Test
        @EnabledOnJre(JRE.JAVA_8)
            void soloJdk8(){
        }

        @Test
        void imprimirSystemProperties(){
            Properties properties = System.getProperties();
            properties.forEach((k , v) -> System.out.println("k = " + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*17.*")
        void testJavaVersion(){
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void solo64(){
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void No64(){
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev") //Solo se ejecuta cuando sea desarrollo
        void testDev(){
        }
}