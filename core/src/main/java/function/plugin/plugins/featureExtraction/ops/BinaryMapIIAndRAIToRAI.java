/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package function.plugin.plugins.featureExtraction.ops;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.AbstractBinaryMapComputer;
import net.imagej.ops.map.MapComputer;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;

/**
 * {@link MapComputer} from {@link IterableInterval} inputs to
 * {@link RandomAccessibleInterval} outputs.
 *
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @param <EI> element type of inputs
 * @param <EO> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW_PRIORITY)
public class BinaryMapIIAndRAIToRAI<EI1, EI2, EO> extends
	AbstractBinaryMapComputer<EI1, EI2, EO, IterableInterval<EI1>, RandomAccessibleInterval<EI2>, RandomAccessibleInterval<EO>>
	implements Contingent
{

	@Override
	public void compute2(final IterableInterval<EI1> input1,
		final RandomAccessibleInterval<EI2> input2, final RandomAccessibleInterval<EO> output)
	{
		final Cursor<EI1> cursorI1 = input1.localizingCursor();
		final RandomAccess<EI2> raI2 = input2.randomAccess();
		final RandomAccess<EO> raO = output.randomAccess();

		while (cursorI1.hasNext()) {
			cursorI1.fwd();
			raI2.setPosition(cursorI1);
			raO.setPosition(cursorI1);
			getOp().compute2(cursorI1.get(), raI2.get(), raO.get());
		}
	}

	@Override
	public boolean conforms() {
		return Intervals.equalDimensions(out(), in());
	}
}
