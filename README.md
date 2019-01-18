# DrawRate Extension

This configurable feature supports creative use of the Draw() loop. 
It also has a utilitarian purpose in that it enables a high-level means of managing consumption of system resources(CPU, GPU, Memory).

Blurb on `Presentation Control` from the Guide
> The default mode is automatic presentation, the draw method is called as often as possible. The other mode is manual presentation, in which it is the developer's responsibility to request draw to be called.

This feature supports finer control between `PresentationMode.AUTOMATIC` and `PresentationMode.MANUAL`.
It has a base `DrawRate` config that applies to the draw loop generally. 
Additionally, further special cases may be configured(currently, special case 'Minimise' is supported).

NB! Refrain from using presentation control modes 'AUTOMATIC' and 'MANUAL' within your app when this feature is `enabled` because it may cause contra-indications.  
 
## DrawRate
### Usage

```kotlin
val dcDrawRate = DrawConfig("myDrawRate", true, 200L)

extend(DrawRate()) {
    drawRate = dcDrawRate
}
```   

The config has 3 settings
- `id` : any string
- `enable` : true or false
- `delay duration` : a value greater than 0 i.e. between MINDELAY(1L) and MAXDELAY(Long.MAX_VALUE)

Subsequently the draw rate can also be toggled at runtime using the keyboard viz.
`<CTRL_SPACEBAR>`. (The drawing window must be active to receive keyboard input.)



#### Optional runtime config
Further control is supported at runtime e.g.
```
if (seconds.toInt() % 20 == 0)
    dcDrawRate.duration = 1L
if (seconds.toInt() % 20 == 10)
    dcDrawRate.duration = 225L
```
     
       
## Window Event - Special Case 1 
Some window events may cause the width and height to be 0 e.g. MINIMISE. 
In this case the drawing window is not visible. 
In this situation it may make sense to pause the draw loop. 
(Or to reduce the frequency of the draw())

```kotlin
val dcMinimiseRate = DrawConfig("myMinimmiseRate", true, MAXDELAY)

extend(DrawRate()) {
    drawRate = dcMinimiseRate
}
```   

It has the same 3 config settings as DrawRate
- `id` : any string
- `enable` : true or false
- `delay duration` : a value greater than 0 i.e. between MINDELAY(1L) and MAXDELAY(Long.MAX_VALUE). 
MAXDELAY will effectively pause the draw loop. 

This action is triggered by a window `MINIMISE` event. It only goes into effect when the width or height is 0. 
A window 'RESTORE' event resumes the prior draw frequency. 
 
 
## Window Event - Special Case 2
### UNFOCUS - ToDo 