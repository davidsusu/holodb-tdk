<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

use App\Models\Employee;

class EmployeeSeeder extends Seeder
{
    public function run(): void
    {
        DB::statement('ALTER TABLE companies DISABLE KEYS');
        foreach (range(1, 1000) as $batch) {
            Employee::factory()->count(1000)->create();
        }
        DB::statement('ALTER TABLE companies ENABLE KEYS');
    }
}
