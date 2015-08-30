import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
       
        //TODO
		int currentLine = 0;
		int processingIndex = 0;
		List<Integer> processedLines = Arrays.asList(getIndexes());
		Collections.sort(processedLines);
		List<String> ignoredWords = Arrays.asList(stopWordsArray);
		Map<String, Integer> wordsVsCount = new TreeMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(this.inputFileName));
		String line = reader.readLine();
		while (line != null){
			while (processingIndex < processedLines.size() && currentLine == processedLines.get(processingIndex)){
				StringTokenizer tokenizer = new StringTokenizer(line, delimiters);
				while(tokenizer.hasMoreTokens()){
					String word = tokenizer.nextToken().toLowerCase().trim();
					if (!ignoredWords.contains(word)){
						int count = 1;
						if (wordsVsCount.containsKey(word)){
							count = wordsVsCount.get(word) + 1;
						}
						wordsVsCount.put(word, count);
					}
				}
				processingIndex ++;
			}
			line = reader.readLine();
			currentLine ++;
		}
		List<Integer> counts = new ArrayList<Integer>(wordsVsCount.values());
		Collections.sort(counts, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return -1 * o1.compareTo(o2);
			}
		});
		List<Integer> top20 = counts.subList(0, 20);
		for (String word: wordsVsCount.keySet()){
			int wordCount = wordsVsCount.get(word);
			if (top20.contains(wordCount)){
				ret[top20.indexOf(wordCount)] = word;
				top20.set(top20.indexOf(wordCount), Integer.MIN_VALUE);
			}
		}
		return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
