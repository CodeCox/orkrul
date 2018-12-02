# orkrul

This configurable feature supports creative use of the Draw() loop.

`SPACEBAR` toggles the DrawRate on or off  

## DrawRate
config params :
 - enabled (true or false)
 - duration > 0, < Long.MAX_VALUE, or NODRAW(== MAX_VALUE)
 
 default:
 ```
 var drawRate = DrawRateConfig(false, 1000L)
 ```
       
       
## MinRate
if enabled the window minimise event triggers this.
config params :
 - enabled (true or false)
 - duration > 0, < Long.MAX_VALUE, or NODRAW(== MAX_VALUE)

default:
 ```
 var minRate = DrawRateConfig(false, 2000L)
 ```
 
 
## optional custom initialisation that will override default values
```
var drcDR = DrawRateConfig(true, 325L)
var drcM = DrawRateConfig(true, 750L)
```

## optional runtime config
```
if (seconds.toInt() % 20 == 0)
    drcDR.duration = 150L
if (seconds.toInt() % 20 == 10)
    drcDR.duration = 500L
```

### Test Cases
- default config
1. feature totally disabled - app is 'std' mode
2. spacebar toggles DrawRate
3. window minimise does not change above.
4. test for NODRAW and  x > 0 && x < MAX_VALUE 

- enable minRate
1. app is 'std' mode
2. spacebar toggles DrawRate
3. window minimise enabled for both cases of above.
4. window restore resumes prev DrawRate state.
5. test for NODRAW and  x > 0 && x < MAX_VALUE 

- custom initialisation config

as above.

- custom runtime config

as above.

 