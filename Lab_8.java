import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Lab_8 {
    public static void main(String[] args) {
        int[] array = new int[1000000];
        Random random = new Random();
        for(int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(99) + 1;
        }
        System.out.println(Arrays.stream(array).sum());
        CustomRecursiveTask task = new CustomRecursiveTask(array);
        System.out.println(task.compute());
    }

}

class CustomRecursiveTask extends RecursiveTask<Integer> {
    private int[] array;

    private static final int MAX_SIZE = 20;

    CustomRecursiveTask(int[] array) {
        this.array = array;
    }

    @Override
    protected Integer compute() {
        if (array.length > MAX_SIZE) {
            return ForkJoinTask.invokeAll(createSubtasks()).stream().mapToInt(ForkJoinTask::join).sum();
        } else {
            return processing(array);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks() {
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(array, 0, array.length/2)));
        dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(array, array.length/2, array.length)));
        return dividedTasks;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr).sum();
    }
}
