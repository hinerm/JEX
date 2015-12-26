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

package function.plugin.plugins.featureExtraction.ops.features.sets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.command.CommandInfo;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import function.plugin.plugins.featureExtraction.ops.featuresets.AbstractOpRefFeatureSet;
import function.plugin.plugins.featureExtraction.ops.featuresets.DimensionBoundFeatureSet;
import function.plugin.plugins.featureExtraction.ops.featuresets.FeatureSet;
import function.plugin.plugins.featureExtraction.ops.featuresets.NamedFeature;
import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.OpRef;
import net.imagej.ops.features.haralick.HaralickFeature;
import net.imagej.ops.image.cooccurrencematrix.MatrixOrientation2D;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * {@link FeatureSet} for {@link HaralickFeature}s
 * 
 * @author Christian Dietz, University of Konstanz
 * @author Jay Warrick, University of Wisconsin Madison
 * 
 * @param <T>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Haralick 2D Features", description = "Calculates the 2D Haralick Features")
public class Haralick2DFeatureSet<T, O extends RealType<O>> extends AbstractOpRefFeatureSet<IterableInterval<T>, O>
		implements Contingent, DimensionBoundFeatureSet<IterableInterval<T>, O> {

	private static final String PKG = "net.imagej.ops.Ops$Haralick$";

	@Parameter(type = ItemIO.INPUT, label = "Num. Grey Levels", description = "The number of grey values determines the size of the co-occurence matrix on which the Haralick features are calculated.", min = "1", max = "2147483647", stepSize = "1")
	private int numGreyLevels = 32;

	@Parameter(type = ItemIO.INPUT, label = "Distance", description = "The maximum distance between pairs of pixels which will be added to the co-occurence matrix.", min = "1", max = "2147483647", stepSize = "1")
	private int distance = 1;

	@Parameter(type = ItemIO.INPUT, label = "Orientation", description = "Orientation of the pairs of pixels which will be added to the co-occurence matrix", choices = {
			"DIAGONAL", "ANTIDIAGONAL", "HORIZONTAL", "VERTICAL" })
	private String orientation = "HORIZONTAL";

	@Parameter(required = false, label = "ASM", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "ASM") })
	private boolean isASMActive = true;

	@Parameter(required = false, label = "Cluster Promenence", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "ClusterPromenence") })
	private boolean isClusterPromenenceActive = true;

	@Parameter(required = false, label = "Cluster Shade", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "ClusterShade") })
	private boolean isClusterShadeActive = true;

	@Parameter(required = false, label = "Contrast", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "Contrast") })
	private boolean isContrastActive = true;

	@Parameter(required = false, label = "Correlation", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "Correlation") })
	private boolean isCorrelationActive = true;

	@Parameter(required = false, label = "Difference Entropy", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "DifferenceEntropy") })
	private boolean isDifferenceEntropyActive = true;

	@Parameter(required = false, label = "Difference Variance", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "DifferenceVariance") })
	private boolean isDifferenceVarianceActive = true;

	@Parameter(required = false, label = "Entropy", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "Entropy") })
	private boolean isEntropyActive = true;

	@Parameter(required = false, label = "ICM1", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "ICM1") })
	private boolean isICM1Active = true;

	@Parameter(required = false, label = "ICM2", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "ICM2") })
	private boolean isICM2Active = true;

	@Parameter(required = false, label = "IFDM", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "IFDM") })
	private boolean isIFDMActive = true;

	@Parameter(required = false, label = "Max Probability", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "MaxProbability") })
	private boolean isMaxProbabilityActive = true;

	@Parameter(required = false, label = "Sum Average", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "SumAverage") })
	private boolean isSumAverageActive = true;

	@Parameter(required = false, label = "Sum Entropy", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "SumEntropy") })
	private boolean isSumEntropyActive = true;

	@Parameter(required = false, label = "Sum Variance", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "SumVariance") })
	private boolean isSumVarianceActive = true;

	@Parameter(required = false, label = "Texture Homogeneity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "TextureHomogeneity") })
	private boolean isTextureHomogeneityActive = true;

	@Parameter(required = false, label = "Variance", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = "numGreyLevels,distance,orientation"),
			@Attr(name = ATTR_TYPE, value = PKG + "Variance") })
	private boolean isVarianceActive = true;

	@SuppressWarnings("unchecked")
	public void initialize() {
		
		final List<CommandInfo> infos = new ArrayList<CommandInfo>();
		if (prioritizedOps != null) {
			for (final Class<? extends Op> prio : prioritizedOps) {
				final CommandInfo info = new CommandInfo(prio);
				info.setPriority(Priority.FIRST_PRIORITY);
				infos.add(info);
			}
		}
		
		this.namedFeatureMap = new LinkedHashMap<NamedFeature, UnaryFunctionOp<Object, ? extends O>>();

		final Module self = this.cs.getCommand(this.getClass()).createModule(this);
		try {
			for (final ModuleItem<?> item : self.getInfo().inputs()) {
				// we found a feature. lets create a named feature!
				if (item.get(ATTR_FEATURE) != null && ((Boolean) item.getValue(self))) {
					final String[] params = item.get(ATTR_PARAMS).split(",");
					final Object[] args = new Object[params.length];

					int i = 0;
					for (final String param : params) {
						args[i++] = self.getInput(param);
					}
					// make sure we have an outtype
					final Class<? extends O> outType = this.outType == null ? (Class<? extends O>) DoubleType.class
							: this.outType;

					final OpRef<? extends Op> ref = OpRef.create((Class<? extends Op>)Class.forName((String) item.get(ATTR_TYPE)), args);
					
					// Convert the String orientation to a MatrixOrientation
					Object[] currentArgs = ref.getArgs();
					Object[] newArgs = new Object[currentArgs.length];
					int j = 0;
					for(Object o : currentArgs)
					{
						if(o instanceof String)
						{
							newArgs[j++] = MatrixOrientation2D.valueOf((String) o);
						}
						else
						{
							newArgs[j++] = o;
						}
					}

					namedFeatureMap.put(new NamedFeature(ref), Functions.unary(ops(), ref.getType(), outType, in(), newArgs));
				}
			}
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}
}
