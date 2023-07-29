import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class HippodromeTest {
    private Hippodrome hippodrome;
    @Test
    void constructorTest1() {
        Throwable t = assertThrows(
                IllegalArgumentException.class,
                () -> hippodrome = new Hippodrome(null),
                "Expected new Hippodrome(null) throws IllegalArgumentException, but it didn't"
        );
        assertEquals("Horses cannot be null.", t.getMessage());
    }
    @Test
    void constructorTest2() {
        Throwable t = assertThrows(
                IllegalArgumentException.class,
                () -> hippodrome = new Hippodrome(new ArrayList<Horse>()),
                "Expected new Hippodrome(new ArrayList<Horse>()) throws IllegalArgumentException, but it didn't"
        );
        assertEquals("Horses cannot be empty.", t.getMessage());
    }

    @Test
    void getHorses() {
        ArrayList<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            horses.add(new Horse("horse" + i, 0));
        }
        hippodrome = new Hippodrome(horses);
        assertEquals(horses, hippodrome.getHorses());
    }

    @Test
    void move() {
        ArrayList<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            horses.add(Mockito.mock(Horse.class));
        }
        hippodrome = new Hippodrome(horses);
        hippodrome.move();
        for (int i = 0; i < horses.size(); i++) {
            Mockito.verify(hippodrome.getHorses().get(i), times(1)).move();
        }

    }

    @Test
    void getWinner() {
        Random rand = new Random();
        ArrayList<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            horses.add(new Horse("horse" + i, 0, rand.nextInt(1000)));
        }
        hippodrome = new Hippodrome(horses);
        Horse expected = horses.stream()
                .max(Comparator.comparing(Horse::getDistance))
                .get();
        assertEquals(expected, hippodrome.getWinner());
    }
}