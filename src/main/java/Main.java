import heuristics.value.DefaultValueOrder;
import heuristics.value.LeastConstraintValue;
import heuristics.value.ValueHeuristics;
import heuristics.variable.MinimumRemainingOptions;
import heuristics.variable.VariableHeuristics;
import model.Sudoku;
import model.SudokuSolver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import utils.Utils;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

        int[][] values;

        //empty
        values = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };


        //extreme
//        values = new int[][]{
//                {0, 0, 0, 0, 3, 0, 8, 0, 0},
//                {0, 0, 0, 4, 0, 0, 0, 1, 0},
//                {6, 0, 4, 0, 0, 0, 5, 0, 0},
//                {0, 0, 0, 3, 7, 9, 0, 0, 0},
//                {1, 5, 0, 0, 0, 8, 0, 0, 0},
//                {2, 0, 0, 0, 0, 1, 0, 0, 0},
//                {0, 0, 1, 0, 0, 0, 0, 7, 9},
//                {0, 9, 0, 8, 0, 0, 0, 0, 3},
//                {0, 7, 0, 0, 0, 0, 0, 0, 0}
//        };

        //extreme
//        values = new int[][]{
//                {0, 0, 0, 0, 7, 0, 3, 0, 0},
//                {0, 0, 5, 8, 9, 0, 0, 0, 0},
//                {2, 4, 8, 0, 0, 0, 0, 0, 0},
//                {0, 9, 0, 0, 0, 0, 0, 0, 4},
//                {0, 0, 1, 2, 0, 6, 7, 0, 0},
//                {5, 0, 0, 0, 0, 0, 0, 2, 0},
//                {0, 0, 0, 0, 0, 0, 8, 7, 6},
//                {0, 0, 0, 0, 1, 4, 5, 0, 0},
//                {0, 0, 7, 0, 5, 0, 0, 0, 0}
//        };

        //hard
//        values = new int[][]{
//                {0, 0, 8, 0, 5, 4, 7, 0, 0},
//                {0, 0, 0, 8, 3, 0, 0, 2, 0},
//                {1, 0, 0, 2, 0, 0, 0, 0, 3},
//                {4, 0, 0, 0, 0, 0, 5, 0, 7},
//                {0, 0, 0, 0, 9, 0, 0, 0, 0},
//                {6, 0, 3, 0, 0, 0, 0, 0, 9},
//                {3, 0, 0, 0, 0, 1, 0, 0, 2},
//                {0, 2, 0, 0, 7, 3, 0, 0, 0},
//                {0, 0, 1, 5, 8, 0, 6, 0, 0}
//        };

        //hard
//        values = new int[][]{
//                {0, 8, 0, 0, 2, 3, 4, 0, 0},
//                {3, 0, 0, 0, 0, 6, 0, 2, 5},
//                {0, 0, 7, 0, 0, 0, 0, 0, 0},
//                {0, 7, 0, 0, 0, 1, 0, 0, 0},
//                {0, 0, 6, 5, 0, 4, 3, 0, 0},
//                {0, 0, 0, 3, 0, 0, 0, 9, 0},
//                {0, 0, 0, 0, 0, 0, 5, 0, 0},
//                {8, 5, 0, 6, 0, 0, 0, 0, 9},
//                {0, 0, 2, 9, 1, 0, 0, 3, 0}
//        };
        int totalBacktracks = 0;
        int solvedWithoutBacktracks = 0;

        for (int p = 0; p < 1; p++) {
            WebDriver driver = new EdgeDriver();
            driver.get("https://www.websudoku.com/?select=1&level=4");
            driver.switchTo().frame(0);

            Utils.sleep(1000);

            driver.findElement(By.xpath("//input[@type='submit']")).click();
            Utils.sleep(3000);

            WebElement table = driver.findElement(By.id("puzzle_grid"));

            for (int i = 0; i < 9; i++) {
                WebElement tr = table.findElements(By.xpath(".//tbody//tr")).get(i);
                for (int j = 0; j < 9; j++) {
                    WebElement td = tr.findElements(By.xpath(".//td//input")).get(j);
                    int n = 0;
                    try {
                        n = Integer.parseInt(td.getAttribute("value"));
                    } catch (NumberFormatException e) {
                    }

                    values[i][j] = n;
                }
            }

            driver.quit();

            Sudoku sudoku = new Sudoku(values);

            VariableHeuristics variableHeuristics = new MinimumRemainingOptions();
            ValueHeuristics valueHeuristics = new LeastConstraintValue();

            SudokuSolver solver = new SudokuSolver(sudoku, mainFrame, variableHeuristics, valueHeuristics);

            mainFrame.setVisible(true);

            solver.solve();

            System.out.println("Number of Backtracks: " + solver.backtrackN);
            totalBacktracks += solver.backtrackN;
            if (solver.backtrackN == 0)
                solvedWithoutBacktracks++;
        }
//        System.out.println("Total number of backtracks = " + totalBacktracks);
//        System.out.println("Number of sudokus solved without backtracking = " + solvedWithoutBacktracks);
    }
}
