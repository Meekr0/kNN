import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        int depth = Integer.parseInt(args[0]);
        String trainFileName = args[1];
        String testFileName = args[2];

        int dimensions = 1;
        boolean dimensionsKnown = false;

        float testIrisCount = 0f;
        float correctGuessCount = 0f;
        float accuracy;


        try{

            BufferedReader trainFileReader = new BufferedReader(new FileReader(trainFileName+".csv"));
            BufferedReader testFileReader = new BufferedReader(new FileReader(testFileName+".csv"));

            List<Iris> trainingIrisList = new ArrayList<>();
            List<Iris> testIrisList = new ArrayList<>();

            String line;
            while((line = trainFileReader.readLine()) != null) {

                if(!dimensionsKnown) {

                    dimensions = findDim(line, ',');
                    dimensionsKnown = true;

                }

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);
                trainingIrisList.add(iris);

            }
            while((line = testFileReader.readLine()) != null) {

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);
                testIrisList.add(iris);
                testIrisCount++;

            }

            //Collections.shuffle(trainingIrisList);
            //Collections.shuffle(testIrisList);

            trainFileReader.close();
            testFileReader.close();

            //TEST SET
            for(Iris unknownIris : testIrisList) {

                System.out.println("Now Checking Iris: " + unknownIris.getIrisInfoWithoutSpoilers());

                String guessedIris = guessIris(unknownIris, depth, trainingIrisList);
                String correctIris = unknownIris.getType();

                if(checkIfCorrect(correctIris, guessedIris)) {

                    System.out.println("Prediction Was Correct" + "\n");
                    correctGuessCount++;

                } else
                    System.out.println("Prediction Was Incorrect" + "\n");

            }

            accuracy = (correctGuessCount * 100f) / testIrisCount;
            System.out.println("Overall Accuracy = " + accuracy + "% over " + testIrisCount + " guesses." + "\n");

            //USER INPUT
            while(true) {

                Scanner userInputScanner = new Scanner(System.in);
                line = userInputScanner.nextLine();
                System.out.println(line);

                String[] lineContents = line.split(",");
                double[] currentIrisParams = new double[dimensions];

                for(int i = 0; i < dimensions; i++)
                    currentIrisParams[i] = Double.parseDouble(lineContents[i]);

                Iris iris = new Iris(lineContents[dimensions], currentIrisParams);

                String guessedIris = guessIris(iris, depth, trainingIrisList);
                String correctIris = iris.getType();

                if(checkIfCorrect(correctIris, guessedIris))
                    System.out.println("Prediction Was Correct" + "\n");
                else
                    System.out.println("Prediction Was Incorrect" + "\n");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int findDim(String s, char c) {

        int x = 0;

        for(int i=0; i<s.length(); i++)
            if(s.charAt(i) == c)
                x++;

        return x;

    }

    public static double findDistance(Iris unknownIris, Iris knownIris) {

        double[] unknownIrisParams = unknownIris.getParams();
        double[] knownIrisParams = knownIris.getParams();
        int dimensions = unknownIrisParams.length;

        double sum = 0;
        for(int i = 0; i < dimensions; i++) {

            sum += Math.pow((unknownIrisParams[i] - knownIrisParams[i]), 2);

        }

        //sum = Math.sqrt(sum);

        return sum;

    }

    public static String guessIris(Iris unknownIris, int depth, List<Iris> trainingIrisList) {

        double distance;
        for(Iris knownIris : trainingIrisList) {

            distance = findDistance(unknownIris, knownIris);
            knownIris.setDistanceFromNewIris(distance);

        }

        Collections.sort(trainingIrisList, (o1, o2) -> {
            if(o1.getDistanceFromNewIris() < o2.getDistanceFromNewIris())
                return -1;
            else if(o1.getDistanceFromNewIris() > o2.getDistanceFromNewIris())
                return 1;
            else return 0;
        });

        Map<String, Integer> irisTypesMap = new TreeMap<>();

        for(int i = 0; i < depth; i++) {

            Iris irisToPutInMap = trainingIrisList.get(i);

            String mapKey = irisToPutInMap.getType();
            if (irisTypesMap.containsKey(mapKey)) {
                for (Map.Entry<String, Integer> e : irisTypesMap.entrySet())
                    if (e.getKey().equals(mapKey))
                        e.setValue(e.getValue() + 1);
            } else
                irisTypesMap.put(mapKey, 1);

        }

        Map.Entry<String, Integer> mostCommonType = null;
        for(Map.Entry<String, Integer> e : irisTypesMap.entrySet())
            if(mostCommonType == null || e.getValue().compareTo(mostCommonType.getValue()) > 0)
                mostCommonType = e;

        return mostCommonType.getKey();


    }

    public static boolean checkIfCorrect(String correctIris, String guessedIris) {

        System.out.println("Prediction For This Iris: " + guessedIris);
        System.out.println("The Correct Answer Was: " + correctIris);
        return guessedIris.equals(correctIris);

    }

}
