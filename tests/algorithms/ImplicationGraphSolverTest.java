package algorithms;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImplicationGraphSolverTest {


    private void testImplicationGraph(List<Integer>[] adj, List<Integer>[] adjInv, boolean debug) {
        ImplicationGraphSolver solver = new ImplicationGraphSolver();
        boolean[] solution = solver.getSolution(adj, adjInv);
        validateSolution(adj, solution, debug);
    }

    private void validateSolution(List<Integer>[] adj, boolean[] solution, boolean debug) {
        int n = solution.length;
        if (debug) {
            System.out.println("solution: ");
            for (int i = 0; i < n; i++) {
                System.out.println(i + ": " + solution[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (solution[i]) {
                for (Integer j : adj[i]) {
                    if (j < n) {
                        assertTrue(solution[j], i + " is true therefore " + j + " should be true");
                    } else {
                        assertTrue(!solution[j - n], i + " is true therefore " + j + " should be false");
                    }
                }
            } else {
                for (Integer j : adj[i + n]) {
                    if (j < n) {
                        assertTrue(solution[j], i + " is false therefore " + j + " should be true");
                    } else {
                        assertTrue(!solution[j - n], i + " is false therefore " + j + " should be false");
                    }
                }
            }
        }
    }


    private void testList(int[] input, int noNodes) {
        int length = input.length / 2;
        List<Integer>[] adj = new List[noNodes];
        List<Integer>[] adjInv = new List[noNodes];

        for (int i = 0; i < noNodes; i++) {
            adj[i] = new ArrayList<>();
            adjInv[i] = new ArrayList<>();
        }

        for (int i = 0; i < length; i++) {
            adj[input[i * 2]].add(input[i * 2 + 1]);
            adjInv[input[i * 2 + 1]].add(input[i * 2]);
        }

        testImplicationGraph(adj, adjInv, true);
    }

    @Test
    public void testSecondGraph() {
        testList(new int[]{0, 1, 0, 5, 2, 3, 2, 1, 3, 2, 4, 3, 4, 5, 5, 0}, 6);
    }

    @Test
    public void testFirstGraph() {
        testList(new int[]{0, 1, 2, 3}, 4);
    }
}
