<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;

use App\Models\Company;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Employee>
 */
class EmployeeFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'company_id' => rand(1, Company::count()),
            'firstname' => $this->faker->firstName(),
            'lastname' => $this->faker->lastName(),
            'birth_year' => $this->faker->year(),
            'birth_country' => $this->faker->country(),
            'preferred_color' => $this->faker->safeColorName(),
        ];
    }
}
