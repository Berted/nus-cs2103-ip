package duke.storage;

import duke.DukeException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.TaskList;
import duke.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class handles the storage of user data into a file.
 */
public class Storage {

    private final File saveFile;

    /**
     * Constructs Storage with a specified File for Storage to access.
     * @param saveFile the specified File.
     */
    public Storage(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * Constructs Storage with a specified file path for Storage to access.
     * @param filePath the specified file path.
     */
    public Storage(String filePath) {
        this.saveFile = new File(filePath);
    }

    /**
     * Loads tasks from the encapsulated File onto a TaskList.
     * @return TaskList.
     * @throws DukeException if unable to read from the encapsulated File.
     */
    public TaskList load() throws DukeException {
        try {
            Scanner sc = new Scanner(this.saveFile);
            TaskList ret = new TaskList();
            while (sc.hasNext()) {
                String[] curTask = sc.nextLine().split(" / ");
                // duke.task.Task type saved in the third parameter
                switch (curTask[2]) {
                    case "T":
                        ret.addTask(new Todo(curTask[0], curTask[1].equals("true")));
                        break;
                    case "D":
                        ret.addTask(new Deadline(curTask[0], curTask[3], curTask[1].equals("true")));
                        break;
                    case "E":
                        ret.addTask(new Event(curTask[0], curTask[3], curTask[1].equals("true")));
                        break;
                    default:
                        throw new DukeException("Invalid save file data. Will ignore save file.");
                }
            }
            return ret;
        } catch (FileNotFoundException e) {
            try {
                this.saveFile.getParentFile().mkdirs();
                this.saveFile.createNewFile();
            } catch (IOException e1) {
                throw new DukeException(String.format("Unable to create save-file at %s.", this.saveFile.getPath()), e1);
            } catch (SecurityException e2) {
                throw new DukeException(String.format("Write access denied to save file. Please make sure TedBot has access to %s.", this.saveFile.getPath()), e2);
            }
            throw new DukeException(String.format("No save file found. A new save file is generated at %s.", this.saveFile.getPath()));
        }
    }

    /**
     * Stores tasks from the specified TaskList parameter to the encapsulated File.
     * @param taskList the specified TaskList.
     * @throws DukeException if unable to write to the encapsulated File.
     */
    public void save(TaskList taskList) throws DukeException {
        try {
            FileWriter fw = new FileWriter(this.saveFile);
            for (int i = 0; i < taskList.getLength(); i++) {
                fw.write(taskList.getTask(i).toStorageString() + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new DukeException(String.format("Unable to write to save file at %s. Last change was not added to savefile.", this.saveFile.getPath()), e);
        }
    }
}
