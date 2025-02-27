################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/Isthmus\ C.cpp 

CPP_DEPS += \
./src/Isthmus\ C.d 

OBJS += \
./src/Isthmus\ C.o 


# Each subdirectory must supply rules for building sources it contributes
src/Isthmus\ C.o: ../src/Isthmus\ C.cpp src/subdir.mk
	@echo 'Building file: $<'
	@echo 'Invoking: Cygwin C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"src/Isthmus C.d" -MT"$@" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


clean: clean-src

clean-src:
	-$(RM) ./src/Isthmus\ C.d ./src/Isthmus\ C.o

.PHONY: clean-src

