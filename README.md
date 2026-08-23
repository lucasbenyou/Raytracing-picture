# Raytracing Picture

A ray tracer written from scratch in Java — no graphics library: every point, vector,
intersection formula and lighting equation is implemented and unit-tested by hand.

Mini Project — Introduction to Software Engineering (ISE5783_6686_6223).

![Two gorillas in a jungle](images/gorillasJungle.png)

## Features

- **Primitives** — `Point`, `Vector`, `Ray`, with exponent-based floating point accuracy (`Util`)
- **Geometries** — plane, sphere, triangle, polygon, tube, cylinder, and a composite `Geometries`
- **Camera** — view plane, ray construction, PNG output
- **Phong reflection model** — ambient, directional, point and spot lights, diffuse and specular terms
- **Shadows** — shadow rays, including partial shadows through translucent objects
- **Transparency and reflection** — recursive secondary rays with adaptive cut-off
- **Anti-aliasing** — regular supersampling and adaptive supersampling with a pixel-corner cache
- **Performance** — multi-threaded rendering, one image row per task

## The final picture

Two gorillas built **only from polygons** (138 each) in a jungle of 80 more polygons,
lit by a spot light through translucent leaves — transparency, partial shadows and a
reflective water sheet.

Run `JungleGorillasTests.twoGorillasInTheJungle()`; the image is written to
`images/gorillasJungle.png`.

## Documentation

The full technical write-up — every stage of the project, from the primitives to the
anti-aliased final image, with the algorithms, the test strategy and the performance
measurements — is in **[RAYTRACING_PROJECT.md](RAYTRACING_PROJECT.md)**.

## Layout

```
src/         primitives, geometries, lighting, renderer, scene
unitTests/   19 test classes, 50 test methods
images/      rendered output
```

