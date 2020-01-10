package misc;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

import knapsack.Knapsack;
import knapsack.Knapsack2;
import knapsack.parcel.Parcels;

public final class CompareMethods {
	
	public static final Runtime runtime = Runtime.getRuntime();
	
	public static long getMemoryUsage() {
		return runtime.totalMemory() - runtime.freeMemory();
	}
	
	public static long getDeltaMemoryUsage(Runnable execution) {
		long start = 0l;
		long delta = 0l;
		Thread t = new Thread(execution);
		runtime.gc();
		start = getMemoryUsage();
		t.start();
		try { t.join(); } catch (Exception e) {}
		delta = getMemoryUsage() - start;
		return delta;
	}
	
	public static <T> MemoryResult<T> getDeltaMemoryResult(Supplier<T> method, boolean accurate_memory) {
		long start = 0l;
		long delta = 0l;
		T result;
		long start_time = System.currentTimeMillis();
		if (accurate_memory) runtime.gc();
		start = getMemoryUsage();
		result = method.get();
		delta = getMemoryUsage() - start;
		long delta_time = System.currentTimeMillis() - start_time;
		return new MemoryResult<T>(result, delta, delta_time);
	}
	
	static class MemoryResult<Result> {
		public Result result;
		long memory_usage;
		long delta_time;
		public <T extends Result> MemoryResult(T result, long memory_usage, long delta_time) {
			this.result = result;
			this.memory_usage = memory_usage;
			this.delta_time = delta_time;
		}
	}
	
	static class InOutputBatch<InputType, OutputType> {
		public InputType input;
		public OutputType output_1;
		public OutputType output_2;
		public final boolean equal;
		public <X extends InputType, Y extends OutputType> InOutputBatch(X input, Y output_1, Y output_2) {
			this.input = input;
			this.output_1 = output_1;
			this.output_2 = output_2;
			equal = output_1.equals(output_2);
		}
	}
	
	static class TimeAndMemoryComparison {
		long time_1;
		long time_2;
		long memory_1;
		long memory_2;
		public void add(long delta_time_1, long delta_time_2, long delta_memory_1, long delta_memory_2) {
			time_1 += delta_time_1;
			time_2 += delta_time_2;
			memory_1 += delta_memory_1;
			memory_2 += delta_memory_2;
		}
	}
	
	static class Comparison<InputType, OutputType> {
		final ArrayList<InOutputBatch<? extends InputType, ? extends OutputType>> batches;
		final TimeAndMemoryComparison times;
		public Comparison() {
			batches = new ArrayList<InOutputBatch<? extends InputType, ? extends OutputType>>();
			times = new TimeAndMemoryComparison();
		}
		public <X extends InputType, Y1 extends OutputType, Y2  extends OutputType> void add(
				X input, MemoryResult<Y1> result_1, MemoryResult<Y2> result_2) {
			batches.add(new InOutputBatch<InputType, OutputType>(input, result_1.result, result_2.result));
			times.add(result_1.delta_time, result_2.delta_time, result_1.memory_usage, result_2.memory_usage);
		}
		public double partEqual() {
			int total = 0;
			for (InOutputBatch<?,?> batch : batches) if (batch.equal) total++;
			return ((double)total)/batches.size();
		}
		public double inputDuplicates() {
			int total_duplicates = 0;
			ArrayList<InputType> inputs = new ArrayList<InputType>();
			for (InOutputBatch<? extends InputType,? extends OutputType> batch : batches) {
				if (!inputs.contains(batch.input)) inputs.add(batch.input);
				else total_duplicates++;
			} return ((double)total_duplicates)/batches.size();
		}
		@Override
		public String toString() {
			String evaluation = " [results of comparison for "+batches.size()+" inputs] \n";
			evaluation += "Function-1:\n - time used = "+times.time_1+"ms ("+((100*(float)times.time_1)/(times.time_1+times.time_2))+"% of total)\n";
			evaluation += " - memory used = "+times.memory_1/1024+"kb ("+((100*(float)times.memory_1)/(times.memory_1+times.memory_2))+"% of total)\n\n";
			evaluation += "Function-2:\n - time used = "+times.time_2+"ms ("+((100*(float)times.time_2)/(times.time_1+times.time_2))+"% of total)\n";
			evaluation += " - memory used = "+times.memory_2/1024+"kb ("+((100*(float)times.memory_2)/(times.memory_1+times.memory_2))+"% of total)\n\n";
			evaluation += "Of all inputs that were given, "+(100*(float)inputDuplicates())+"% were equal\n";
			evaluation += "Of all the outputs from Function-1 and Function-2, "+(100*(float)partEqual())+"% were equal\n";
			return evaluation;
		}
	}
	
	/**
	 * @param <Input> input-type of the functions
	 * @param <Output> output-type of the functions
	 * @param function_1 should not mutate the input
	 * @param function_2 should not mutate the input
	 * @param input should generate different inputs so that the comparison can be better evaluated
	 * @param minimal_duration_ms minimal duration of the comparison
	 * @param memory_focus greatly improves accuracy of memory usage measurements, but severely impacts run-speed (which can reduce accuracy 
	 *  of time usage measurements when not enough time is taken to make more measurements)
	 */
	public static <Input, Output> Comparison<Input, Output> compareOutputs(
			Function<? super Input, ? extends Output> function_1,
			Function<? super Input, ? extends Output> function_2,
			Supplier<? extends Input> input, long minimal_duration_ms, boolean memory_focus) {
		Comparison<Input, Output> comparison = new Comparison<Input, Output>();
		long delta_time = 0;
		while (delta_time <= minimal_duration_ms) {
			Input in = input.get();
			MemoryResult<Output> result_f1 = getDeltaMemoryResult(()->{ return function_1.apply(in); }, memory_focus);
			MemoryResult<Output> result_f2 = getDeltaMemoryResult(()->{ return function_2.apply(in); }, memory_focus);
			comparison.add(in, result_f1, result_f2);
			delta_time += result_f1.delta_time + result_f2.delta_time;
		} return comparison;
	}
	
	public static void main(String[] args) {
		Knapsack k1 = new Knapsack();
		Knapsack2 k2 = new Knapsack2();
		System.out.println("comparing f1(fit a parcel with BigInteger) to f2(fit a parcel with boolean[])\n"+
				compareOutputs(
				a->{return Boolean.valueOf(k1.fitsParcel(a));},
				a->{return Boolean.valueOf(k2.fitsParcel(a));},
				()->{return Parcels.randomSimpleParcels(k1, 1, 1)[0];},
				5000l, true
				));
		System.out.println("comparing f1(create knapsack with BigInteger) to f2(create knapsack with boolean[])\n"+
				compareOutputs(
				a->{return new Knapsack(randInt(10,30),randInt(10,30),randInt(10,30));},
				a->{return new Knapsack2(randInt(10,30),randInt(10,30),randInt(10,30));},
				()->{return "";},
				5000l, true
				));
	}
	
	public static int randInt(int min, int max) {
		return (int)(Math.random() * (max-min+1) + min);
	}
	
}
