package net.wohlfart.gl.elements;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;

public class Skybox implements Renderable, SkyboxParameters {

	private IMesh[] sides;

	private final int size = 1024;

	private final PerlinNoiseParameters clouds = new PerlinNoiseParameters() {

		@Override
		public int getOctaves() {
			return 5;
		}

		@Override
		public float getPersistence() {
			return 0.5f;
		}

		@Override
		public float getFrequency() {
			return 1f;
		}

		@Override
		public float getW() {
			return 0.5f;
		}

	};

	private final PerlinNoiseParameters stars = new PerlinNoiseParameters() {

		@Override
		public int getOctaves() {
			return 5;
		}

		@Override
		public float getPersistence() {
			return 0.7f;
		}

		@Override
		public float getFrequency() {
			return 3f;
		}

		@Override
		public float getW() {
			return 1;
		}

	};


	public void init() {
		SkyboxSide[] values = SkyboxSide.values();
		List<IMesh> array = new ArrayList<IMesh>(values.length);
		for (SkyboxSide side : values) {
			array.add(side.build(this));
		}
		sides = array.toArray(new IMesh[values.length]);
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public void render() {
		if (sides == null) {
			init();
		}
		// FIXME: draw only the visible sides
		for (IMesh side : sides) {
			side.draw();
		}
	}

	@Override
	public void dispose() {
		sides = null;
	}

	@Override
	public PerlinNoiseParameters getNoiseParamClouds() {
		return clouds;
	}

	@Override
	public PerlinNoiseParameters getNoiseParamStars() {
		return stars;
	}

}
