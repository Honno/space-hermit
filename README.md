# Space Hermit

![Going faster than light](/doc/hermit.gif)

This is my submission for the 2nd year graphics module in my CS degree. We were tasked to create a scene using LWJGL/OpenGL with the theme of "space" which demonstrated your learning in lectures and labs. I decided to simulate the experience of warp travel inside a cockpit.

The project requires LWJGL2.0+/3.0+ (https://www.lwjgl.org/).

Attribution, licenses and the coursework spec can be found in the *doc* folder.
  
## Features

* The cockpit "bobs" in the x, y and z axis with the use of sinusoidal movement to create a feeling of floating in space.
* Translucent, almost-holographic depictions of the Earth and Moon occupying the cockpit. 
* The moon orbits the Earth, with sinusoidal bumps in the y axis to simulate fluctuations.
* A lever that animates forwards and backwards, using sinusoidal movement to simulate a "resistance" effect when pushing.
* You can push the lever and start the warp protocol. The shaking and ambience exemplifies, with the scene fading to a pure white as you travel across the universe.
* Hologram flickers for a period after a warp to simulate a "stabalising" effect.
