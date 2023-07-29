import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HorseTest {
    @Mock
    private Horse horse;
    @DisplayName("Проверка имя лошади: не null")
    @Test
    @Order(1)
    void constructorTest1() {
        Throwable t = assertThrows(
                IllegalArgumentException.class,
                () -> horse = new Horse(null, 10),
                "Expected new Horse(null, 10) throws IllegalArgumentException, but it didn't"
        );
        assertEquals("Name cannot be null.", t.getMessage());
    }
    @DisplayName("Проверка имя лошади: не пустое и не пробелы")
    @ParameterizedTest(name = "\"{0}\" - throws IllegalArgumentException")
    @ValueSource(strings = { "", "  " })
    @Order(2)
    void constructorTest2(String name) {
        Throwable t = assertThrows(
                IllegalArgumentException.class,
                () -> horse = new Horse(name, 10),
                "Expected param name throws IllegalArgumentException, but it didn't"
        );
        assertEquals("Name cannot be blank.", t.getMessage());
    }
    @DisplayName("Проверка скорости лошади: не отрицательная")
    @Test
    @Order(3)
    void constructorTest3() {
        Throwable t = assertThrows(
                IllegalArgumentException.class,
                () -> horse = new Horse("horse", -1),
                "Expected negative param speed throws IllegalArgumentException, but it didn't"
        );
        assertEquals("Speed cannot be negative.", t.getMessage());
    }
    @DisplayName("Проверка дистанции лошади: не отрицательная")
    @Test
    @Order(4)
    void constructorTest4() {
        Throwable t = assertThrows(
                IllegalArgumentException.class,
                () -> horse = new Horse("horse", 10, -1),
                "Expected negative param distance throws IllegalArgumentException, but it didn't"
        );
        assertEquals("Distance cannot be negative.", t.getMessage());
    }

    @DisplayName("Проверка правильности метода getName()")
    @Test
    @Order(5)
    void getNameTest() {
        try {
            horse = new Horse("horse", 10);
            final Field field = horse.getClass().getDeclaredField("name");
            field.setAccessible(true);
            String expectedName = "Sharik";
            field.set(horse, expectedName);
            final String result = horse.getName();
            assertEquals(expectedName, result, "Expected horse.getName() ==  expectedName, but it didn't");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Проверка правильности метода getSpeed()")
    @Test
    @Order(6)
    void getSpeedTest() {
        try {
            horse = new Horse("horse", 10);
            final Field field = horse.getClass().getDeclaredField("speed");
            field.setAccessible(true);
            double expectedSpeed = 15;
            field.set(horse, expectedSpeed);
            double result = horse.getSpeed();
            assertEquals(expectedSpeed, result, "Expected horse.getSpeed() ==  expectedSpeed, but it didn't");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Проверка правильности метода getDistance()")
    @Test
    @Order(7)
    void getDistance() {
        horse = new Horse("horse", 10);
        double expectedDistance = 0;
        double result = horse.getDistance();
        assertEquals(expectedDistance, result, "Expected horse.getDistance() ==  expectedDistance, but it didn't");
    }

    @DisplayName("Проверка правильности метода move()")
    @Test
    @Order(8)
    void move() {
        // создаем статический мок
        try (MockedStatic<Horse> theMock = Mockito.mockStatic(Horse.class)) {
            // создаем реальный объект
            horse = new Horse("horse", 10, 20);
            // вызываем метод move()
            horse.move();
            // проверяем отработал ли метод getRandomDouble(0.2, 0.9) в методе move()
            theMock.verify(() -> Horse.getRandomDouble(0.2, 0.9));
            // мокаем статический метод Horse.getRandomDouble чтобы возвращал нужное значение
            theMock.when(() -> Horse.getRandomDouble(0.2, 0.9))
                    .thenReturn((Math.random() * (0.9 - 0.2)) + 0.2);
            // рассчитываем правильный ответ
            double expected = 10 * Horse.getRandomDouble(0.2, 0.9) + 20;
            // запускаем метод move()
            horse.move();
            // сравниваем рассчеты дистанции
            assertEquals(expected, horse.getDistance());
        } catch (IllegalArgumentException e) {

        }
    }
}