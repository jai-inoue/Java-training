package ch03.ex15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class LatentImage {
	private final Image in;
	private final Color[][] parallelIn;
	private final List<UnaryOperatorAdapter<Color>> pendingOperations;

	private LatentImage(final Color[][] image, final Image in) {
		this.in = in;
		this.parallelIn = image;
		this.pendingOperations = new ArrayList<>();
	}

	public static LatentImage from(final Image image) {
		final Color[][] colors = convertToMatrix(image);
		return new LatentImage(colors, image);
	}

	public LatentImage transform(final UnaryOperator<Color> f) {
		pendingOperations.add(new UnaryOperatorAdapter<>(f, false));
		return this;
	}

	public LatentImage parallelTransform(final UnaryOperator<Color> f) {
		pendingOperations.add(new UnaryOperatorAdapter<>(f, true));
		return this;
	}

	public Image toImage() {
		if (pendingOperations.size() < 1) {
			return in;
		}
		Image result = in;
		Color[][] resultParallel = parallelIn;
		final List<ListAdapter<UnaryOperator<Color>>> list = convertListAdapterList(pendingOperations);
		for (final ListAdapter<UnaryOperator<Color>> l : list) {
			if (l.isParallel()) {
				executeParallel(resultParallel, l.get());
				result = convertToImage(resultParallel);
			} else {
				result = execute(result, l.get());
				resultParallel = convertToMatrix(result);
			}
		}
		return result;
	}

	private List<ListAdapter<UnaryOperator<Color>>> convertListAdapterList(
			final List<UnaryOperatorAdapter<Color>> pendingOperations) {
		final List<ListAdapter<UnaryOperator<Color>>> list = new ArrayList<>();
		final ListAdapter<UnaryOperator<Color>> listAdapter = new ListAdapter<>(pendingOperations.get(0).isParallel());
		list.add(listAdapter);
		listAdapter.add(pendingOperations.get(0).getUnaryOperator(), pendingOperations.get(0).isParallel());
		for (final UnaryOperatorAdapter<Color> f : pendingOperations) {
			if (list.get(list.size() - 1).isParallel() != f.isParallel()) {
				list.add(new ListAdapter<>(f.isParallel()));
			}
			list.get(list.size() - 1).add(f.getUnaryOperator(), f.isParallel());
		}
		return list;
	}

	private Image execute(final Image in, final List<UnaryOperator<Color>> pendingOperations) {
		final int width = (int) in.getWidth();
		final int height = (int) in.getHeight();
		final WritableImage out = new WritableImage(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color c = in.getPixelReader().getColor(x, y);
				for (final UnaryOperator<Color> f : pendingOperations) {
					c = f.apply(c);
				}
				out.getPixelWriter().setColor(x, y, c);
			}
		}
		return out;
	}

	private void executeParallel(final Color[][] out, final List<UnaryOperator<Color>> pendingOperations) {
		final int height = out.length;
		final int width = out[0].length;
		final int n = Runtime.getRuntime().availableProcessors();
		try {
			final ExecutorService pool = Executors.newCachedThreadPool();
			for (int i = 0; i < n; i++) {
				final int fromY = i * height / n;
				final int toY = (i + 1) * height / n;
				pool.submit(() -> {
					for (int x = 0; x < width; x++) {
						for (int y = fromY; y < toY; y++) {
							for (final UnaryOperator<Color> f : pendingOperations) {
								out[y][x] = f.apply(out[y][x]);
							}
						}
					}
				});
			}
			pool.shutdown();
			pool.awaitTermination(1, TimeUnit.HOURS);
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private Image convertToImage(final Color[][] out) {
		final int height = out.length;
		final int width = out[0].length;
		final WritableImage result = new WritableImage(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				result.getPixelWriter().setColor(x, y, out[y][x]);
			}
		}
		return result;
	}

	private static Color[][] convertToMatrix(final Image image) {
		final int height = (int) image.getHeight();
		final int width = (int) image.getWidth();
		final Color[][] colors = new Color[height][width];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				colors[y][x] = image.getPixelReader().getColor(x, y);
			}
		}
		return colors;
	}

	class ListAdapter<T> {
		private final List<T> list;
		private final Boolean parallel;

		public ListAdapter(final boolean parallel) {
			this.list = new ArrayList<>();
			this.parallel = parallel;
		}

		public boolean add(final T t, final boolean parallel) {
			if (this.parallel == null || this.parallel == parallel) {
				return this.list.add(t);
			}
			throw new IllegalArgumentException();
		}

		public int size() {
			return list.size();
		}

		public boolean isParallel() {
			return parallel;
		}

		public List<T> get() {
			return list;
		}
	}

	class UnaryOperatorAdapter<T> {
		private final UnaryOperator<T> unaryOperator;
		private final boolean parallel;

		public UnaryOperatorAdapter(final UnaryOperator<T> unaryOperator, final boolean parallel) {
			this.unaryOperator = unaryOperator;
			this.parallel = parallel;
		}

		public UnaryOperator<T> getUnaryOperator() {
			return unaryOperator;
		}

		public boolean isParallel() {
			return parallel;
		}
	}
}
