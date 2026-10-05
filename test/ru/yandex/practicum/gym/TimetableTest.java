package ru.yandex.practicum.gym;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession singleTrainingSession = new TrainingSession(
                group,
                coach,
                DayOfWeek.MONDAY,
                new TimeOfDay(13, 0)
        );

        timetable.addNewTrainingSession(singleTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Map<TimeOfDay, List<TrainingSession>> tuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, monday.size());
        assertTrue(tuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group(
                "Акробатика для взрослых",
                Age.ADULT,
                90
        );

        TrainingSession thursdayAdultTrainingSession =
                new TrainingSession(
                        groupAdult,
                        coach,
                        DayOfWeek.THURSDAY,
                        new TimeOfDay(20, 0)
                );

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group(
                "Акробатика для детей",
                Age.CHILD,
                60
        );

        TrainingSession mondayChildTrainingSession =
                new TrainingSession(
                        groupChild,
                        coach,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(13, 0)
                );

        TrainingSession thursdayChildTrainingSession =
                new TrainingSession(
                        groupChild,
                        coach,
                        DayOfWeek.THURSDAY,
                        new TimeOfDay(13, 0)
                );

        TrainingSession saturdayChildTrainingSession =
                new TrainingSession(
                        groupChild,
                        coach,
                        DayOfWeek.SATURDAY,
                        new TimeOfDay(10, 0)
                );

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Map<TimeOfDay, List<TrainingSession>> thursday =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        Map<TimeOfDay, List<TrainingSession>> tuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, monday.size());
        assertEquals(2, thursday.size());
        assertTrue(tuesday.isEmpty());

        List<TimeOfDay> times =
                new ArrayList<>(thursday.keySet());

        assertEquals(13, times.get(0).getHours());
        assertEquals(20, times.get(1).getHours());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession singleTrainingSession =
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(13, 0)
                );

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> sessionsAt13 =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(13, 0)
                );

        List<TrainingSession> sessionsAt14 =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(14, 0)
                );

        assertEquals(1, sessionsAt13.size());
        assertSame(singleTrainingSession, sessionsAt13.get(0));

        assertTrue(sessionsAt14.isEmpty());
    }

    @Test
    void testMultipleTrainingSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coachOne = new Coach("Иванов", "Иван", "Иванович");
        Coach coachTwo = new Coach("Петров", "Петр", "Петрович");

        Group groupOne = new Group("Группа 1", Age.ADULT, 60);
        Group groupTwo = new Group("Группа 2", Age.ADULT, 60);

        TrainingSession sessionOne = new TrainingSession(
                groupOne,
                coachOne,
                DayOfWeek.MONDAY,
                new TimeOfDay(18, 0)
        );

        TrainingSession sessionTwo = new TrainingSession(
                groupTwo,
                coachTwo,
                DayOfWeek.MONDAY,
                new TimeOfDay(18, 0)
        );

        timetable.addNewTrainingSession(sessionOne);
        timetable.addNewTrainingSession(sessionTwo);

        List<TrainingSession> sessions =
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(18, 0)
                );

        assertEquals(2, sessions.size());
        assertSame(sessionOne, sessions.get(0));
        assertSame(sessionTwo, sessions.get(1));
    }


    @Test
    void testTrainingSessionsSortedByHoursAndMinutes() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.FRIDAY,
                        new TimeOfDay(15, 45)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.FRIDAY,
                        new TimeOfDay(10, 30)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.FRIDAY,
                        new TimeOfDay(10, 15)
                )
        );

        Map<TimeOfDay, List<TrainingSession>> sessions =
                timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);

        List<TimeOfDay> times =
                new ArrayList<>(sessions.keySet());

        assertEquals(10, times.get(0).getHours());
        assertEquals(15, times.get(0).getMinutes());

        assertEquals(10, times.get(1).getHours());
        assertEquals(30, times.get(1).getMinutes());

        assertEquals(15, times.get(2).getHours());
        assertEquals(45, times.get(2).getMinutes());
    }


    @Test
    void testTrainingSessionsFromDifferentDaysDoNotMix() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(12, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.TUESDAY,
                        new TimeOfDay(12, 0)
                )
        );

        Map<TimeOfDay, List<TrainingSession>> monday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        Map<TimeOfDay, List<TrainingSession>> tuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(1, monday.size());
        assertEquals(1, tuesday.size());

        assertEquals(
                DayOfWeek.MONDAY,
                monday.values().iterator().next().get(0).getDayOfWeek()
        );

        assertEquals(
                DayOfWeek.TUESDAY,
                tuesday.values().iterator().next().get(0).getDayOfWeek()
        );
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(10, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.WEDNESDAY,
                        new TimeOfDay(15, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coach,
                        DayOfWeek.FRIDAY,
                        new TimeOfDay(20, 0)
                )
        );

        Map<String, Integer> result =
                timetable.getCountByCoaches();

        assertEquals(1, result.size());
        assertEquals(
                3,
                result.get("Иванов Иван Иванович")
        );
    }


    @Test
    void testGetCountByCoachesSortedDescending() {
        Timetable timetable = new Timetable();

        Coach ivanov = new Coach("Иванов", "Иван", "Иванович");
        Coach petrov = new Coach("Петров", "Петр", "Петрович");
        Coach sidorov = new Coach("Сидоров", "Сидор", "Сидорович");

        Group group = new Group("Группа", Age.ADULT, 60);

        // Иванов — 2 тренировки
        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        ivanov,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(10, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        ivanov,
                        DayOfWeek.TUESDAY,
                        new TimeOfDay(10, 0)
                )
        );

        // Петров — 3 тренировки
        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        petrov,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(11, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        petrov,
                        DayOfWeek.WEDNESDAY,
                        new TimeOfDay(11, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        petrov,
                        DayOfWeek.FRIDAY,
                        new TimeOfDay(11, 0)
                )
        );

        // Сидоров — 1 тренировка
        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        sidorov,
                        DayOfWeek.SUNDAY,
                        new TimeOfDay(12, 0)
                )
        );

        Map<String, Integer> result =
                timetable.getCountByCoaches();

        List<String> coaches =
                new ArrayList<>(result.keySet());

        assertEquals("Петров Петр Петрович", coaches.get(0));
        assertEquals("Иванов Иван Иванович", coaches.get(1));
        assertEquals("Сидоров Сидор Сидорович", coaches.get(2));

        assertEquals(3, result.get("Петров Петр Петрович"));
        assertEquals(2, result.get("Иванов Иван Иванович"));
        assertEquals(1, result.get("Сидоров Сидор Сидорович"));
    }


    @Test
    void testGetCountByCoachesSameFioCountsAsSameCoach() {
        Timetable timetable = new Timetable();

        Coach coachOne =
                new Coach("Иванов", "Иван", "Иванович");

        Coach coachTwo =
                new Coach("Иванов", "Иван", "Иванович");

        Group group = new Group("Группа", Age.ADULT, 60);

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coachOne,
                        DayOfWeek.MONDAY,
                        new TimeOfDay(10, 0)
                )
        );

        timetable.addNewTrainingSession(
                new TrainingSession(
                        group,
                        coachTwo,
                        DayOfWeek.TUESDAY,
                        new TimeOfDay(10, 0)
                )
        );

        Map<String, Integer> result =
                timetable.getCountByCoaches();

        assertEquals(1, result.size());

        assertEquals(
                2,
                result.get("Иванов Иван Иванович")
        );
    }

}
