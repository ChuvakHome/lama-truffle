//package ru.itmo.truffle.lama.test;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.Set;
//
//import org.graalvm.polyglot.Context;
//
//import ru.itmo.truffle.lama.LamaLanguage;
//
//public class LamaTestMain {
//	public static void main(String[] args) throws IOException {
////		File file = new File(args[0]);
//		
//		File[] files = new File("/Users/aaamoj/Desktop/ITMO/Дисциплины/Магистратура/2025_2026/3_семестр/Языковые_виртуальные_машины/lab2/lama_interpreter/deps/Lama/tests/performance/").listFiles();
//		
////		Context.create(LamaLanguage.ID);
//		
//		Set<String> skippedTest = Set.of(
//					"test074.lama",
//					"test092.lama",
//					"test095.lama",
//					"test098.lama",
//					"test105.lama",
//					"test111.lama"
//				);
//		
//		for (File file: files) {
//			String testfileName = file.getName(); 
//			
//			if (testfileName.endsWith(".lama") && !skippedTest.contains(testfileName)) {
//				System.out.println("Test: " + file.getName());
//				
//				String source = String.join("\n", Files.readAllLines(file.toPath()));
//				
//				File inputFile = new File(file.getAbsoluteFile().toString().replace(".lama", ".input"));
//				
//				if (inputFile.exists()) {
//					System.setIn(new FileInputStream(inputFile));
//				}
//				
//				System.out.println(source);
//				
//				Context ctx = Context.newBuilder(LamaLanguage.ID).allowAllAccess(true).build();
//				ctx.eval(LamaLanguage.ID, source);
//			}
//		}
//				
////		Context ctx = Context.newBuilder(LamaLanguage.ID).allowAllAccess(true).build();
////		String s = "var n = read ();\n"
////				+ "\n"
////				+ "do\n"
////				+ "  \n"
////				+ "  if n == 1 then write (0)\n"
////				+ "elif n == 2 then write (1)\n"
////				+ "elif n == 3 then write (2)\n"
////				+ "elif n == 4 then write (3)\n"
////				+ "            else write (10)\n"
////				+ "  fi;\n"
////				+ "\n"
////				+ "  if n >= 5 then write (11) fi;\n"
////				+ "\n"
////				+ "  n := n - 1\n"
////				+ "\n"
////				+ "while n != 0 od";
//		
////		ctx.eval(LamaLanguage.ID, s);
//	}
//}
