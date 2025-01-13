<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;

use App\Models\Employee;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Coupon>
 */
class CouponFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'employee_id' => rand(1, Employee::count()),
            'identifier' =>
                chr($this->faker->numberBetween(65, 70)) .
                chr($this->faker->numberBetween(65, 70)) .
                chr($this->faker->numberBetween(65, 70)) .
                '-' .
                $this->faker->numberBetween(100000000000, 999999999999)
            ,
        ];
    }
}
