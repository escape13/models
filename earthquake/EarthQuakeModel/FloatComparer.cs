namespace EarthQuakeModel
{
    /// <summary>
    /// Класс для сравнения вещественных чисел.
    /// </summary>
    internal static class FloatComparer
    {
        /// <summary>
        /// Точность сравнения
        /// </summary>
        private const float _precision = 0.0001f;

        /// <summary>
        /// Сравивает, больше или равно значение числа какого-то базового значения.
        /// </summary>
        /// <param name="BaseValue">Базовое значение</param>
        /// <param name="Value">Сравниваемое значение</param>
        /// <returns>true - если сравнивоемое значение больше или равно базового.</returns>
        public static bool IsGreatherOrEqual(float BaseValue, float Value)
        {
            return Value - BaseValue >= _precision;
        }
    }
}
