package ru.yandex.practicum.gym;

import java.util.*;


public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
    }

    /*
        Нам не важно, в каком порядке хранятся внутри списка занятия, так как важно получить чисто набор. Поэтому ArrayList будем использовать.
        Далее алгоритм такой:
            1. Взять dow из trainingSession
            2. Найти TreeMap этого дня
            3. Взять tod из trainingSession
            4. Найти List для этого времени
            5. Добавить trainingSession в этот List
         */

    // компаратор для TreeMap
    private final Comparator<TimeOfDay> timeComparator = new Comparator<>() {
        @Override
        public int compare(TimeOfDay first, TimeOfDay second) {
            if (first.getHours() != second.getHours()) {
                return Integer.compare(first.getHours(), second.getHours());
            }

            return Integer.compare(first.getMinutes(), second.getMinutes());
        }
    };

    public void addNewTrainingSession(TrainingSession trainingSession) {
        // сохраняем занятие в расписании
        // получим timeOfDay для вот этой структуры: TreeMap<main_java.TimeOfDay, List<main_java.TrainingSession>
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        // получим dayOfWeek для вот этой структуры: HashMap<main_java.DayOfWeek, TreeMap<>>
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();

        if (timetable.containsKey(dayOfWeek)) {
            Map<TimeOfDay, List<TrainingSession>> timetableOnDay = timetable.get(dayOfWeek);

            if (timetableOnDay.containsKey((timeOfDay))) {
                List<TrainingSession> listOfTrainingSessions = timetableOnDay.get(timeOfDay);
                listOfTrainingSessions.add(trainingSession);

            } else {
                List<TrainingSession> listOfTrainingSessions = new ArrayList<>();
                listOfTrainingSessions.add(trainingSession);
                timetableOnDay.put(timeOfDay, listOfTrainingSessions);
            }


        } else {
            List<TrainingSession> listOfTrainingSessions = new ArrayList<>();
            listOfTrainingSessions.add(trainingSession);
            Map<TimeOfDay, List<TrainingSession>> middleTreeMap = new TreeMap<>(timeComparator);
            middleTreeMap.put(timeOfDay, listOfTrainingSessions);
            timetable.put(dayOfWeek, middleTreeMap);
        }
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, new TreeMap<>(timeComparator));
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> timetableOnDay = timetable.get(dayOfWeek);

        if (timetableOnDay == null) {
            return new ArrayList<>();
        }
        List<TrainingSession> trainingSessions = timetableOnDay.get(timeOfDay);

        if (trainingSessions == null) {
            return new ArrayList<>();
        }

        return trainingSessions;
    }

    //public getCountByCoaches

    public Map<String, Integer> getCountByCoaches() {
        Map<String, Integer> counts = new HashMap<>();
        // Считаем тренировки каждого тренера
        for (Map<TimeOfDay, List<TrainingSession>> timetableOnDay : timetable.values()) {
            for (List<TrainingSession> sessions : timetableOnDay.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    String fio = coach.getSurname()
                            + " " + coach.getName()
                            + " " + coach.getMiddleName();

                    if (counts.containsKey(fio)) {
                        counts.put(fio, counts.get(fio) + 1);
                    } else {
                        counts.put(fio, 1);
                    }
                }
            }
        }

        // Получаем список всех тренеров
        List<String> coaches = new ArrayList<>(counts.keySet());
        // Сортируем тренеров по количеству тренировок
        coaches.sort(new Comparator<String>() {
            @Override
            public int compare(String first, String second) {
                return Integer.compare(
                        counts.get(second),
                        counts.get(first)
                );
            }
        });

        // Сохраняем результат в нужном порядке
        Map<String, Integer> result = new LinkedHashMap<>();
        for (String coach : coaches) {
            result.put(coach, counts.get(coach));
        }

        return result;
    }

}
