package duke.command;

import duke.DukeException;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * This command encapsulates an index value and marks the Task as not completed
 * at the encapsulated index in the task list when executed.
 */
public class UnmarkCommand extends Command {

    /** The keyword to run UnmarkCommand. */
    public static final String COMMAND_NAME = "unmark";

    private final int unmarkIndex;

    /**
     * Constructs an UnmarkCommand object encapsulating the specified index value.
     * @param unmarkIndex The index of the Task to be marked as not completed.
     */
    public UnmarkCommand(int unmarkIndex) {
        this.unmarkIndex = unmarkIndex;
    }

    /**
     * Marks the Task as completed at the encapsulated index in the specified TaskList object.
     * <p>
     * In addition, the appropriate response will be sent to the specified Ui parameter
     * and the specified Storage parameter will also be updated with the new TaskList values.
     * @param tasks the specified TaskList object.
     * @param ui the specified Ui object.
     * @param storage the specified Storage object.
     * @throws DukeException if the provided index value is out-of-bounds,
     *     or if tasks are unable to be saved into Storage.
     */
    @Override
    public void exec(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        try {
            if (tasks.getTask(this.unmarkIndex).getIsDone()) {
                tasks.getTask(this.unmarkIndex).setDone(false);
                ui.showReply(String.format("Alright, I've marked this task as not done:\n  %s", tasks.getTask(this.unmarkIndex)));
            } else {
                ui.showReply(String.format("Sorry, but it seems you haven't marked this task as done:\n  %s", tasks.getTask(this.unmarkIndex)));
            }
            storage.save(tasks);
        }  catch (IndexOutOfBoundsException e) {
            throw new DukeException("I do not have a task with that number in my list.", e);
        }
    }

    /**
     * Returns false as UnmarkCommand is not a terminating Command.
     *
     * @return false.
     */
    @Override
    public boolean isTerminator() {
        return false;
    }
}
